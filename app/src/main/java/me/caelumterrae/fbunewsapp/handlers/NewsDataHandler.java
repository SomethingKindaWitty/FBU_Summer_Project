package me.caelumterrae.fbunewsapp.handlers;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.adapters.RelatedAdapter;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.model.Post;

public class NewsDataHandler extends JsonHttpResponseHandler {

    TextView tvBody;
    RelatedAdapter relatedAdapter;
    ArrayList<Post> posts;
    TopNewsClient topNewsClient;
    ProgressBar pb;
    String articleUrl;
    public NewsDataHandler(String articleUrl, TextView tvBody, RelatedAdapter relatedAdapter, ArrayList<Post> posts, TopNewsClient topNewsClient, ProgressBar pb) {
        this.articleUrl = articleUrl;
        this.tvBody = tvBody;
        this.relatedAdapter = relatedAdapter;
        this.posts = posts;
        this.topNewsClient = topNewsClient;
        this.pb = pb;
        pb.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            // the response object also has a keywords passed to it. I am not currently doing anything with the keywords.
            // TODO: use keywords to get related articles.
            JSONArray keywords = response.getJSONArray("keywords");
            String category = response.getString("category");
            String text = response.getString("text");
            tvBody.setText(text);
            pb.setVisibility(ProgressBar.INVISIBLE);

            //TODO: update the trump keyword to be the keyword received from the call to our backend
            topNewsClient.getRelatedNews(category, new RelatedHandler(articleUrl,relatedAdapter, posts));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        System.out.println("failure");
    }
}
