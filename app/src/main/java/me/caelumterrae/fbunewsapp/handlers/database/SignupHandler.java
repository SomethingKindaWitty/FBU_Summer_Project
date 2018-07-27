package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.activities.LoginActivity;
import me.caelumterrae.fbunewsapp.activities.PoliticalActivity;
import me.caelumterrae.fbunewsapp.adapters.RelatedAdapter;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.handlers.RelatedHandler;
import me.caelumterrae.fbunewsapp.model.Post;

// This handler gets called in: LoginActivity onSubmit of sign up button
// This handler: uses intent to move user to PoliticalActivity with UID packaged inside
public class SignupHandler extends JsonHttpResponseHandler {

    Context context;
    public SignupHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            int UID = response.getInt("UID");
            if (UID != -1) {
                // start next intent to Political Activity
                // TODO extract uid from bundle
                final Intent intent = new Intent(context, PoliticalActivity.class);
                intent.putExtra("uid", UID);
                context.startActivity(intent);
            }
            else {
                // error!!
                Toast.makeText(context, "User already exists", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("SignupHandler","failure");
    }
}