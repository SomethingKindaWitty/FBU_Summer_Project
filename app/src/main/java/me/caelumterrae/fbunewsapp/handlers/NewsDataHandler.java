package me.caelumterrae.fbunewsapp.handlers;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
    HashMap<String, String> sourceBias;
    Context context;

    public NewsDataHandler(String articleUrl, TextView tvBody, RelatedAdapter relatedAdapter, ArrayList<Post> posts, TopNewsClient topNewsClient, ProgressBar pb, Context context) {
        this.articleUrl = articleUrl;
        this.tvBody = tvBody;
        this.relatedAdapter = relatedAdapter;
        this.posts = posts;
        this.topNewsClient = topNewsClient;
        this.sourceBias = topNewsClient.sourceBias;
        this.pb = pb;
        this.context = context;
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

            topNewsClient.getRelatedNews(category, new RelatedHandler(articleUrl,relatedAdapter, posts, sourceBias, context));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        System.out.println("failure");
    }
}
