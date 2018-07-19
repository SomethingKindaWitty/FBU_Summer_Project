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
import me.caelumterrae.fbunewsapp.file.PoliticalAffData;
import me.caelumterrae.fbunewsapp.math.Probability;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.utilities.FeedAdapter;

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
                String bias = sourceBias.get(TopNewsClient.trimUrl(post.getUrl()));
                post.setPoliticalBias(TopNewsClient.biasToNum(bias));
                Log.i(TAG, TopNewsClient.trimUrl(post.getUrl()) + " " + Integer.toString(TopNewsClient.biasToNum(bias)) + " " + bias);
                // **** new change -- add to rawPosts. afterwards, populate timeline based on affiliation
                rawPosts.add(post);
            }
            populateTimeline(rawPosts);
            Log.i(TAG, String.format("Loaded %s posts", results.length()));
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse top posts", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Orders posts based on user's political affilliation -- updates post & adapter
    private void populateTimeline(final ArrayList<Post> rawPosts) {
        // Creates Beta distribution based on on users affiliation number.
        PoliticalAffData data = new PoliticalAffData(context);
        double affiliation = data.getAffiliationNum();
        Probability betaDis = new Probability(affiliation);
        int size = rawPosts.size();
        Log.i("Handler", "Affiliation: " + affiliation);
        for (int i = 0; i < size; i++) {
            int category = betaDis.getCategory();
            Post p = findPostWithCategory(rawPosts, category);
            posts.add(p);

            feedAdapter.notifyItemInserted(posts.size()-1);
        }
    }

    private Post findPostWithCategory(ArrayList<Post> rawPosts, int category) {
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

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e(TAG, "Failed to get data from now playing endpoint", throwable);
    }
}
