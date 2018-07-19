package me.caelumterrae.fbunewsapp.handlers;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.utilities.FeedAdapter;

public class TimelineHandler extends JsonHttpResponseHandler{
    HashMap<String, String> sourceBias;
    ArrayList<Post> posts;
    FeedAdapter feedAdapter;
    public final static String TAG = "TimelineHandler";

    public TimelineHandler(HashMap<String, String> sourceBias, ArrayList<Post> posts, FeedAdapter feedAdapter) {
        this.sourceBias = sourceBias;
        this.posts = posts;
        this.feedAdapter = feedAdapter;
    }


    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // parse the response to Post object
        // add the Post object to the arraylist
        try {
            JSONArray results = response.getJSONArray(TopNewsClient.ROOT_NODE);
            for (int i = 0; i < results.length(); i++) {
                Post post = Post.fromJSON(results.getJSONObject(i));
                // Sets the political bias of a source like "cnbc.com" to 0(left)-100(right)
                String bias = sourceBias.get(TopNewsClient.trimUrl(post.getUrl()));
                post.setPoliticalBias(TopNewsClient.biasToNum(bias));
                Log.i(TAG, TopNewsClient.trimUrl(post.getUrl()) + " " + Integer.toString(TopNewsClient.biasToNum(bias)) + " " + bias);
                // add post and notify adapter that a row was added
                posts.add(post);
                feedAdapter.notifyItemInserted(posts.size()-1); // latest item
            }
            Log.i(TAG, String.format("Loaded %s posts", results.length()));
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse top posts", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e(TAG, "Failed to get data from now playing endpoint", throwable);
    }
}
