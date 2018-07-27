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


// This handler gets called in: LoginActivity login button handler
// This handler: moves user to timeline intent with UID packaged inside
public class LoginHandler extends JsonHttpResponseHandler {

    Context context;
    public LoginHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            int UID = response.getInt("UID");
            Log.e("uid",String.valueOf(UID));
            if (UID != -1) {
                // start next intent to Swipeactivity
                final Intent intent = new Intent(context, SwipeActivity.class);
                intent.putExtra("uid", UID);
                intent.putExtra("source","Login");
                context.startActivity(intent);
            }
            else {
                // error!!
                Toast.makeText(context, "Invalid Login", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("LoginHandler","failure");
    }
}