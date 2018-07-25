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

    public void getData(final String articleUrl, JsonHttpResponseHandler jsonHttpResponseHandler)
            throws UnsupportedEncodingException, JSONException {
        String url = API_BASE_URL + "/getArticle";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("url", articleUrl);
        entity = new StringEntity(jsonObject.toString());

        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
    }

    public void login(final String username, final String password, JsonHttpResponseHandler
            jsonHttpResponseHandler) throws UnsupportedEncodingException, JSONException {
        String url = API_BASE_URL + "/login";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        entity = new StringEntity(jsonObject.toString());
        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
    }

    public void signup(final String username, final String password, JsonHttpResponseHandler
            jsonHttpResponseHandler) throws UnsupportedEncodingException, JSONException {
        String url = API_BASE_URL + "/signup";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        entity = new StringEntity(jsonObject.toString());
        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
    }

    public void addLike(final int uid, final String postBias, final String postUrl,
                        JsonHttpResponseHandler jsonHttpResponseHandler) throws
            UnsupportedEncodingException, JSONException {
        String url = API_BASE_URL + "/like";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("UID", uid);
        jsonObject.put("bias", postBias);
        jsonObject.put("url", postUrl);
        entity = new StringEntity(jsonObject.toString());
        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
    }

    public void removeLike(final int uid, final String postBias, final String postUrl,
                           JsonHttpResponseHandler jsonHttpResponseHandler) throws
            UnsupportedEncodingException, JSONException {
        String url = API_BASE_URL + "/like";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("UID", uid);
        jsonObject.put("bias", postBias);
        jsonObject.put("url", postUrl);
        entity = new StringEntity(jsonObject.toString());
        client.delete(context, url, entity, "application/json", jsonHttpResponseHandler);
    }

    public void getLike(final int uid, final String postUrl, JsonHttpResponseHandler
            jsonHttpResponseHandler) throws UnsupportedEncodingException, JSONException {
        String url = API_BASE_URL + "/like";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("UID", uid);
        jsonObject.put("url", postUrl);
        entity = new StringEntity(jsonObject.toString());
        client.get(context, url, entity, "application/json", jsonHttpResponseHandler);
    }

    public void getUser(final int uid, JsonHttpResponseHandler jsonHttpResponseHandler) throws
            UnsupportedEncodingException, JSONException {
        String url = API_BASE_URL + "/user";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("UID", uid);
        entity = new StringEntity(jsonObject.toString());
        client.get(context, url, entity, "application/json", jsonHttpResponseHandler);
    }




}
