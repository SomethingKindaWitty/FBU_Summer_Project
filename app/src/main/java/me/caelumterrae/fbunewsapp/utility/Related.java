package me.caelumterrae.fbunewsapp.utility;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.adapters.RelatedAdapter;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.file.PoliticalAffData;
import me.caelumterrae.fbunewsapp.math.BetaDis;
import me.caelumterrae.fbunewsapp.model.Post;

public class Related {


    public static HashMap<String, String> sourceBias;

    // Orders posts based on user's political affiliation. Takes in rawPosts (list of all raw posts)
    // and context. Updates post & adapter with the curated posts
    public static void populateTimeline(ArrayList<Post> rawPosts, Context context,
                                        ArrayList<Post> posts, RelatedAdapter relatedAdapter) {
        // Creates Beta distribution based on on users affiliation number.
        PoliticalAffData data = new PoliticalAffData(context);
        double affiliation = data.getAffiliationNum();
        BetaDis betaDis = new BetaDis(affiliation);
        int size = rawPosts.size();
        Log.i("Handler", "Affiliation: " + affiliation);
        for (int i = 0; i < size; i++) {
            int category = betaDis.getCategory();
            Post p = findPostWithCategory(rawPosts, category);
            posts.add(p);
            relatedAdapter.notifyItemInserted(posts.size()-1);
        }
    }
    // Finds a post within rawPosts with a given category [0, 25, 50, 75, 100].
    // Returns the post w/ matching category if found. Otherwise if not found,
    // returns the first post off the rawPosts list (basically a post w/ random category)
    private static Post findPostWithCategory(ArrayList<Post> rawPosts, int category) {
        for (int i = 0; i < rawPosts.size(); i++) {
            Post p = rawPosts.get(i);
            if (p.getPoliticalBias() == category) {
                Log.i("FOUND!","Category: " +
                        category + " News Bias: " + Integer.toString(p.getPoliticalBias()) + " Source=" +
                        p.getUrl());
                rawPosts.remove(i);
                return p;
            }
        }
        // otherwise we didn't find a post with the category, so return the first one in the list
        Post p = rawPosts.get(0);
        rawPosts.remove(0);
        Log.i("RANDOM DRAW","Category: " +
                category + " New Bias: " + Integer.toString(p.getPoliticalBias()) + " Source=" +
                p.getUrl());
        return p;
    }

    /* Populates sourceBias hashmap with key=URL and value=bias.
     * Example output { `
     *  nytimes.com : leftcenter
     *  democracynow.org : left
     *  }
     */
    public static HashMap<String, String> populateBiasHashMap(AsyncHttpClient client) {
        sourceBias = new HashMap<>();
        client.get(TopNewsClient.MEDIA_BIAS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Iterator<?> keys = response.keys();
                    while (keys.hasNext()) { // iterate over JSONObject
                        String key = (String)keys.next();
                        JSONObject valueObject = response.getJSONObject(key);
                        String value = valueObject.getString("bias");
                        if (response.get(key) instanceof JSONObject ) {
                            sourceBias.put(key, value);
                            // Log.i("BOOP", key + " : " + value);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("HashMap", "Failed to parse top posts", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("HashMap", "Failed to get data from now playing endpoint", throwable);
            }
        });
        return sourceBias;
    }
}
