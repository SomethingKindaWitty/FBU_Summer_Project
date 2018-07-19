package me.caelumterrae.fbunewsapp.handlers;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.utilities.RelatedAdapter;

public class RelatedHandler extends JsonHttpResponseHandler{
    String originalurl;
    RelatedAdapter relatedAdapter;
    ArrayList<Post> posts;

    public RelatedHandler(String originalurl, RelatedAdapter relatedAdapter, ArrayList<Post> posts) {
        this.originalurl = originalurl;
        this.relatedAdapter = relatedAdapter;
        this.posts = posts;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // parse the response to Post object
        // add the Post object to the arraylist
        try {
            JSONArray results = response.getJSONArray(TopNewsClient.ROOT_NODE);
            for (int i = 0; i < results.length(); i++) {
                Post post = Post.fromJSON(results.getJSONObject(i));
                if (!post.getUrl().equals(originalurl)) {
                    posts.add(post);
                    // notify adapter that a row was added
                    relatedAdapter.notifyItemInserted(posts.size() - 1); // latest item
                }
            }
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
