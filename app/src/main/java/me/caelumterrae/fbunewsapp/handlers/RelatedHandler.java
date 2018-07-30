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
import me.caelumterrae.fbunewsapp.adapters.RelatedAdapter;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.utility.Format;
import me.caelumterrae.fbunewsapp.utility.Timeline;

public class RelatedHandler extends JsonHttpResponseHandler{
    String originalurl;
    RelatedAdapter relatedAdapter;
    ArrayList<Post> posts;
    HashMap<String, String> sourceBias;
    Context context;

    public RelatedHandler(String originalurl, RelatedAdapter relatedAdapter, ArrayList<Post> posts, HashMap<String, String> sourceBias, Context context) {
        this.originalurl = originalurl;
        this.relatedAdapter = relatedAdapter;
        this.posts = posts;
        this.sourceBias = sourceBias;
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // parse the response to Post object
        // add the Post object to the arraylist
        try {
            JSONArray results = response.getJSONArray(TopNewsClient.ROOT_NODE);
            final ArrayList<Post> rawPosts = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                Post post = Post.fromJSON(results.getJSONObject(i));
                if (!post.getUrl().equals(originalurl)) {
                    String bias = sourceBias.get(Format.trimUrl(post.getUrl()));
                    post.setPoliticalBias(Format.biasToNum(bias));
                    rawPosts.add(post);
                }
            }
            Timeline.populateTimeline(rawPosts, context, posts, relatedAdapter);
            // TODO: populate the Related
            Log.i("TopNewsClient", String.format("Loaded %s posts", results.length()));
        } catch (JSONException e) {
            Log.e("TopNewsClient", "Failed to parse top posts", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("TopNewsClient", "Failed to get data from now playing endpoint", throwable);
    }
}
