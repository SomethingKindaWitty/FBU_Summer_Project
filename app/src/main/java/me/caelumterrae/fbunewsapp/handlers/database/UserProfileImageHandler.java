package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

// This handler gets called in: UserFragment when the camera button is clicked
// This handler: confirms that the user's profile image url was set and refreshes the user
public class UserProfileImageHandler extends JsonHttpResponseHandler {
    Context context;

    public UserProfileImageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            if (response.getBoolean("isSet")) {
                Log.e("UserProfileImageHandler", "success");
                // Refresh user
                CurrentUser.refreshUser();
                Log.e("UserFragment", "user has been refreshed");
            }
            else {
                Toast.makeText(context, "Setting ImageUrl Error", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("UserProfileImageHandler","failure");
    }
}
