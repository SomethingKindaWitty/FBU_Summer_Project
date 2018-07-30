package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.activities.SwipeActivity;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class PoliticalAffHandler extends JsonHttpResponseHandler {
    Context context;

    // This handler gets called in: PoliticalActivity (on submit)
    // This handler: Creates User and moves user to Swipeactivity -- handled in CurrentUser
    public PoliticalAffHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            if (response.getBoolean("isSet")) {
                CurrentUser.createUser(response.getInt("UID"), context, SwipeActivity.class);
            }
            else {
                Toast.makeText(context, "Setting Affiliation Error", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("PoliticalAffHandler","failure");
    }
}
