package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.util.Log;
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
import me.caelumterrae.fbunewsapp.handlers.RelatedHandler;
import me.caelumterrae.fbunewsapp.model.Post;

public class LoginSignupHandler extends JsonHttpResponseHandler {


    public LoginSignupHandler() {

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            // returns ok and users id.
            // TODO how to get response code and pass UID back to caller
            int UID = response.getInt("UID");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("SignupHandler","failure");
    }
}