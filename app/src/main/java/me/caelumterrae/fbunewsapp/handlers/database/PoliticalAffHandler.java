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

// This handler is called in: PoliticalActivity
// This handler: sets the users political affiliation by requesting the backend and then moves them to swipeActivity

public class PoliticalAffHandler extends JsonHttpResponseHandler {
    Context context;

    public PoliticalAffHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            Boolean bool = response.getBoolean("isSet");
            int UID = response.getInt("UID");
            if (bool) {
                // start next intent to Swipeactivity
                final Intent intent = new Intent(context, SwipeActivity.class);
                intent.putExtra("uid", UID);

                // TODO: change to one value
                intent.putExtra("source","Political");
                context.startActivity(intent);
            }
            else {
                // error!!
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
