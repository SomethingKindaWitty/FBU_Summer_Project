package me.caelumterrae.fbunewsapp.utility;


import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import me.caelumterrae.fbunewsapp.adapters.FeedAdapter;
import me.caelumterrae.fbunewsapp.adapters.RelatedAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.BackgroundPostHandler;
import me.caelumterrae.fbunewsapp.math.BetaDis;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

// All functions relating to populating the timeline
public class Timeline {

    // Orders posts based on user's political affiliation. Takes in rawPosts (list of all raw posts)
    // and context. Updates post & adapter with the curated posts
    public static void populateTimeline(ArrayList<Post> rawPosts, Context context,
                                        ArrayList<Post> posts, FeedAdapter feedAdapter) {
        // Creates Beta distribution based on on users affiliation number.
        double affiliation = CurrentUser.getUser().getPoliticalPreference();
        BetaDis betaDis = new BetaDis(affiliation);
        int size = rawPosts.size();
        Log.i("Handler", "Affiliation: " + affiliation);
        for (int i = 0; i < size; i++) {
            int category = betaDis.getCategory();
            Post p = findPostWithCategory(rawPosts, category);
            posts.add(p);
            feedAdapter.notifyItemInserted(posts.size()-1);
        }

    }

    public static void populateTimeline(ArrayList<Post> rawPosts, ParseNewsClient client,
                                        final ArrayList<Post> posts, FeedAdapter feedAdapter) throws UnsupportedEncodingException, JSONException {
        // Creates Beta distribution based on on users affiliation number.
        double affiliation = CurrentUser.getUser().getPoliticalPreference();
        BetaDis betaDis = new BetaDis(affiliation);
        int size = rawPosts.size();
        Log.i("Handler", "Affiliation: " + affiliation);
        for (int i = 0; i < size; i++) {
            int category = betaDis.getCategory();
            Post p = findPostWithCategory(rawPosts, category);
            posts.add(p);
            feedAdapter.notifyItemInserted(posts.size()-1);
        }

        for (int i = 0; i < 10; i++){
            client.getData(posts.get(i).getUrl(), new BackgroundPostHandler(posts, i));
        }

    }

    // for related adapter
    public static void populateTimeline(ArrayList<Post> rawPosts, Context context,
                                        ArrayList<Post> posts, RelatedAdapter relatedAdapter) {
        double affiliation = CurrentUser.getUser().getPoliticalPreference();
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

}
