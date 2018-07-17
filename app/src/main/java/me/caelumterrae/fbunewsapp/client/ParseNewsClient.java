package me.caelumterrae.fbunewsapp.client;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

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

    public void getData(String articleUrl, JsonHttpResponseHandler jsonHttpResponseHandler) throws JSONException, UnsupportedEncodingException {
        String url = API_BASE_URL + "/getArticle";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("url", articleUrl);
        entity = new StringEntity(jsonObject.toString());

        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
    }
}
