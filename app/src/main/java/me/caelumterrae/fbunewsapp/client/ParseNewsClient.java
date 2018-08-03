package me.caelumterrae.fbunewsapp.client;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;


public class ParseNewsClient {
    public final static String TAG = "ParseNewsClient";
    public final static String API_BASE_URL = "https://fbu-api.herokuapp.com";
    public final static String ARTICLE_URL_KEY = "url";
    public final static String GET_ARTICLE_COMMENTS_KEY = "articleUrl";
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
        String url = API_BASE_URL + "/signin";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        entity = new StringEntity(jsonObject.toString());
        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
    }

    public void setAffiliation(final int uid, final double affiliation,
                        JsonHttpResponseHandler jsonHttpResponseHandler) throws UnsupportedEncodingException, JSONException {
        String url = API_BASE_URL + "/setaff";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("UID", uid);
        jsonObject.put("aff", affiliation);
        entity = new StringEntity(jsonObject.toString());
        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
    }

    public void setProfileImage(final int uid, final String url,
                                JsonHttpResponseHandler jsonHttpResponseHandler) throws UnsupportedEncodingException, JSONException {
        String baseurl = API_BASE_URL + "/setimage";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("UID", uid);
        jsonObject.put("image", url);
        entity = new StringEntity(jsonObject.toString());
        client.post(context, baseurl, entity, "application/json", jsonHttpResponseHandler);
        Log.e("ParseNewsClient", "just posted image url");
    }

    public void addLike(final int uid, final double postBias, final String postUrl,
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

    public void removeLike(final int uid, final double postBias, final String postUrl,
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
        String url = API_BASE_URL + "/getlike";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("UID", uid);
        jsonObject.put("url", postUrl);
        entity = new StringEntity(jsonObject.toString());
        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
        Log.e("Backend like","accessed");
    }

    public void getUser(final int uid, JsonHttpResponseHandler jsonHttpResponseHandler) throws
            UnsupportedEncodingException, JSONException {
        String url = API_BASE_URL + "/user";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("UID", uid);
        entity = new StringEntity(jsonObject.toString());
        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
        Log.e("Backend user","accessed");
    }

    public void getComments(final String articleUrl, JsonHttpResponseHandler jsonHttpResponseHandler){
        Log.e("getComments", articleUrl);
        String url = API_BASE_URL + "/comment";
        RequestParams params = new RequestParams();
        params.put(GET_ARTICLE_COMMENTS_KEY, articleUrl);
        client.get(url, params, jsonHttpResponseHandler);
    }

    public void postComment(int UID, String body, String articleUrl, JsonHttpResponseHandler jsonHttpResponseHandler) throws JSONException, UnsupportedEncodingException {
        String url = API_BASE_URL + "/comment";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("UID", UID);
        jsonObject.put("body", body);
        jsonObject.put("articleUrl", articleUrl);
        entity = new StringEntity(jsonObject.toString());
        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
        Log.e("Backend comment", "posted");
    }

    public void getNumComments(int UID, JsonHttpResponseHandler jsonHttpResponseHandler) throws
            UnsupportedEncodingException, JSONException {
        Log.e("getNumComments UID", Integer.toString(UID));
        String url = API_BASE_URL + "/getcomments";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("UID", UID);
        entity = new StringEntity(jsonObject.toString());
        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
    }

    public void getNumLikes(int UID,JsonHttpResponseHandler jsonHttpResponseHandler) throws
            UnsupportedEncodingException, JSONException {
        String url = API_BASE_URL + "/getlikes";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("UID", UID);
        entity = new StringEntity(jsonObject.toString());
        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
    }

}
