package me.caelumterrae.fbunewsapp.handlers;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.model.Post;

public class BackgroundPostHandler extends JsonHttpResponseHandler{

    ArrayList<Post> posts;
    int position;

    public BackgroundPostHandler(ArrayList<Post> posts, int position) {
        this.posts = posts;
        this.position = position;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // the response object also has a keywords passed to it. I am not currently doing anything with the keywords.

        try {JSONArray keywords = null;
            String text = response.getString("text");
            if(posts.size()-1 >= position) {
                posts.get(position).setBody(text);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onSuccess(statusCode, headers, response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
    }
}
