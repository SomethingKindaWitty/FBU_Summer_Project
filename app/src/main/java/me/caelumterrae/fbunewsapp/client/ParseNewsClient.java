package me.caelumterrae.fbunewsapp.client;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import me.caelumterrae.fbunewsapp.handlers.RelatedHandler;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.adapters.RelatedAdapter;

public class ParseNewsClient {
    public final static String TAG = "ParseNewsClient";
    public final static String API_BASE_URL = "https://fbu-api.herokuapp.com";
    public final static String ARTICLE_URL_KEY = "url";
    Context context;

    AsyncHttpClient client;

    public ParseNewsClient(Context context) {
        client = new AsyncHttpClient();
        this.context = context;
    }

    public void getData(final String articleUrl, final TextView tvBody, final RelatedAdapter relatedAdapter, final ArrayList<Post> finalPosts, final TopNewsClient topNewsClient, final ProgressBar pb) throws UnsupportedEncodingException, JSONException {
        String url = API_BASE_URL + "/getArticle";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("url", articleUrl);
        entity = new StringEntity(jsonObject.toString());
        pb.setVisibility(ProgressBar.VISIBLE);

        client.post(context, url, entity, "application/json", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray keywords = response.getJSONArray("keywords");
                    String category = response.getString("category");
                    String text = response.getString("text");
                    tvBody.setText(text); //TODO: Load text into the post so that post doesnt have to be reloaded.
                    pb.setVisibility(ProgressBar.INVISIBLE);

                    //TODO: update the trump keyword to be the keyword received from the call to our backend
                    topNewsClient.getRelatedNews(category, new RelatedHandler(articleUrl,relatedAdapter, finalPosts));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(context, "failure to hit NewsParserClient", Toast.LENGTH_LONG).show();
            }
        });
    }
}
