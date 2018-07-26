package me.caelumterrae.fbunewsapp.handlers;

import android.content.Context;
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
import me.caelumterrae.fbunewsapp.adapters.FeedAdapter;
import me.caelumterrae.fbunewsapp.utility.Format;
import me.caelumterrae.fbunewsapp.utility.Timeline;

public class TimelineHandler extends JsonHttpResponseHandler{
    HashMap<String, String> sourceBias;
    ArrayList<Post> posts;
    FeedAdapter feedAdapter;
    Context context;
    public final static String TAG = "TimelineHandler";

    public TimelineHandler(HashMap<String, String> sourceBias, ArrayList<Post> posts, FeedAdapter feedAdapter, Context context) {
        this.sourceBias = sourceBias;
        this.posts = posts;
        this.feedAdapter = feedAdapter;
        this.context = context;
    }


    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // parse the response to Post object
        // add the Post object to the arraylist
        try {
            JSONArray results = response.getJSONArray(TopNewsClient.ROOT_NODE);

            // rawPosts will get the raw data from the request. We will then order the posts based
            // on the user's political affiliation and put that in posts.
            final ArrayList<Post> rawPosts = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                Post post = Post.fromJSON(results.getJSONObject(i));
                // Sets the political bias of a source like "cnbc.com" to 0(left)-100(right)
                String bias = sourceBias.get(Format.trimUrl(post.getUrl()));
                post.setPoliticalBias(Format.biasToNum(bias));
                Log.i(TAG, Format.trimUrl(post.getUrl()) + " " + Integer.toString(Format.biasToNum(bias)) + " " + bias);
                // Add to rawPosts. afterwards, populate timeline based on affiliation
                rawPosts.add(post);
            }
            Timeline.populateTimeline(rawPosts, context, posts, feedAdapter);
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
