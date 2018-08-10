package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.activities.SwipeActivity;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

// This handler gets called in: Political Activity in on submit button listener
// This handler: creates user from Json and sets to current user, and then moves to Swipe Activity
public class SignupHandler extends JsonHttpResponseHandler {

    Context context;
    public SignupHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            int UID = response.getInt("UID");
            Log.e("UID", Integer.toString(UID));
            if (UID != -1) {
                User user = User.fromJSON(response);
                Log.e("User", "created");
                CurrentUser.createUser(user, context, SwipeActivity.class);
            }
            else {
                Toast.makeText(context, "Signup Error - User already exists", Toast.LENGTH_LONG).show();
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