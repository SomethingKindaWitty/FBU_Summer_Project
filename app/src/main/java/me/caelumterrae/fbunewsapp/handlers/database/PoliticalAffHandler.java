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

public class PoliticalAffHandler extends JsonHttpResponseHandler {
    Context context;

    public PoliticalAffHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            Boolean bool = response.getBoolean("isSet");
            // TODO- change backend to also give back uid
            int UID = response.getInt("UID");
            if (bool) {
                // start next intent to Swipeactivity
                final Intent intent = new Intent(context, SwipeActivity.class);
                intent.putExtra("uid", UID);
                intent.putExtra("source","Political");
                context.startActivity(intent);
            }
            else {
                // error!!
                Toast.makeText(context, "Setting Affiliation Error", Toast.LENGTH_LONG);
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
