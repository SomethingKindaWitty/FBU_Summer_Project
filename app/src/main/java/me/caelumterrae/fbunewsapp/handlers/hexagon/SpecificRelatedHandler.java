package me.caelumterrae.fbunewsapp.handlers.hexagon;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.utility.Format;

public class SpecificRelatedHandler extends JsonHttpResponseHandler{
    private ArrayList<ArrayList<Post>> postMap;
    private HashMap<String, String> sourceBias;
    ParseNewsClient client;
    int x, y;

    public SpecificRelatedHandler(ArrayList<ArrayList<Post>> postMap, HashMap<String, String> sourceBias, ParseNewsClient client, int x, int y){
        this.postMap = postMap;
        this.sourceBias = sourceBias;
        this.client = client;
        this.x = x;
        this.y = y;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // parse the response to Post object
        // add the Post object to the arraylist
        try {
            JSONArray results = response.getJSONArray(TopNewsClient.ROOT_NODE);
            // rawPosts will get the raw data from the request. Put the thing in the right place and update the posts.
            final ArrayList<Post> rawPosts = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                Post post = Post.fromJSON(results.getJSONObject(i));
                // Sets the political bias of a source like "cnbc.com" to 0(left)-100(right)
                String bias = sourceBias.get(Format.trimUrl(post.getUrl()));
                post.setPoliticalBias(Format.biasToNum(bias));
                post.getKeywords(client);
                // Add to rawPosts
                rawPosts.add(post);
            }
            //After adding the post, call the function on all surrounding posts, just this for now though.
            postMap.get(x).set(y, rawPosts.get(0));
        } catch (JSONException e) {
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
