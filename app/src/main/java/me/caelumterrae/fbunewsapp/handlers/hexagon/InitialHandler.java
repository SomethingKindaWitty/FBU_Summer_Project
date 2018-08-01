package me.caelumterrae.fbunewsapp.handlers.hexagon;

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
import me.caelumterrae.fbunewsapp.utility.Format;

public class InitialHandler extends JsonHttpResponseHandler{
    private ArrayList<ArrayList<Post>> postMap;
    private HashMap<String, String> sourceBias;

    public InitialHandler(ArrayList<ArrayList<Post>> postMap, HashMap<String, String> sourceBias){
        this.postMap = postMap;
        this.sourceBias = sourceBias;
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
                // Add to rawPosts. afterwards, populate timeline based on affiliation
                rawPosts.add(post);
            }
            //now add the posts to the original 4
            postMap.get(5).set(7, rawPosts.get(0));
            postMap.get(5).set(6, rawPosts.get(1));
            postMap.get(5).set(8, rawPosts.get(2));
            postMap.get(4).set(7, rawPosts.get(3));
        } catch (JSONException e) {

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
