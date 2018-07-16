package me.caelumterrae.fbunewsapp.client;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.FeedAdapter;
import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.model.Post;

public class TopNewsClient extends AppCompatActivity {

    public static final String API_KEY = "403530691b5d4a39bcc661496af91ce8";
    public final static String API_KEY_PARAM = "apiKey";
    public final static String API_BASE_URL = "https://newsapi.org/v2"; // base API url
    public final static String COUNTRY_KEY_PARAM = "country";
    public final static String COUNTRY = "us";
    public final static String ROOT_NODE = "articles";
    AsyncHttpClient client;

    // Instantiates new Top News Client that extracts hottest news posts from NewsApi.org

    public TopNewsClient() {
        client = new AsyncHttpClient(); // TODO: close
    }

    // Retrieves ArrayList of posts of top news from newsapi.org APi
    // Pass in feedAdapter and this function will populate it with top news articles
    public void getTopNews(final FeedAdapter feedAdapter, final ArrayList<Post> posts) {
        String url = API_BASE_URL + "/top-headlines"; // create url -- endpoint is /sources
        RequestParams params = new RequestParams();
        params.put(COUNTRY_KEY_PARAM, COUNTRY);
        params.put(API_KEY_PARAM, API_KEY); // TODO: Make Api Key Secret

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // parse the response to Post object
                // add the Post object to the arraylist
                try {
                    JSONArray results = response.getJSONArray(ROOT_NODE);
                    for (int i = 0; i < results.length(); i++) {
                        Post post = Post.fromJSON(results.getJSONObject(i));
                        posts.add(post);
                        // notify adapter that a row was added
                        feedAdapter.notifyItemInserted(posts.size()-1); // latest item
                    }
                    Log.i("TopNewsClient", String.format("Loaded %s posts", results.length()));
                } catch (JSONException e) {
                    Log.e("TopNewsClient", "Failed to parse top posts", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TopNewsClient", "Failed to get data from now playing endpoint", throwable);
            }
        });

    }
}
