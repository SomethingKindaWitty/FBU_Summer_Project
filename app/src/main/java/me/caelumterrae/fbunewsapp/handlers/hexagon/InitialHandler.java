package me.caelumterrae.fbunewsapp.handlers.hexagon;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.utility.Format;

public class InitialHandler extends JsonHttpResponseHandler{
    private ArrayList<ArrayList<Post>> postMap;
    private HashMap<String, String> sourceBias;
    ParseNewsClient client;
    TopNewsClient topNewsClient;

    public InitialHandler(ArrayList<ArrayList<Post>> postMap, HashMap<String, String> sourceBias, Context context){
        this.postMap = postMap;
        this.sourceBias = sourceBias;
        this.client = new ParseNewsClient(context);
        this.topNewsClient = new TopNewsClient();
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
                // Add to rawPosts
                rawPosts.add(post);
            }
            //now add the posts to the original 4
            postMap.get(5).set(7, rawPosts.get(0));
            postMap.get(5).set(6, rawPosts.get(1));
            postMap.get(5).set(8, rawPosts.get(2));
            postMap.get(4).set(7, rawPosts.get(3));
            postMap.get(4).set(6, rawPosts.get(4));
            postMap.get(4).set(8, rawPosts.get(5));
            postMap.get(6).set(7, rawPosts.get(6));
            postMap.get(6).set(8, rawPosts.get(7));
            postMap.get(6).set(6, rawPosts.get(8));
//            //get the keywords for these four and then after generating keywords attempt to populate surrounding.
//            postMap.get(5).get(7).getKeywords(client, topNewsClient, postMap, 5, 7);
//            postMap.get(5).get(6).getKeywords(client, topNewsClient, postMap, 5, 6);
//            postMap.get(5).get(8).getKeywords(client, topNewsClient, postMap, 5, 8);
//            postMap.get(4).get(7).getKeywords(client, topNewsClient, postMap, 4, 7);
        } catch (JSONException e) {
        } catch (ParseException e) {
            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
        }
    }
}
