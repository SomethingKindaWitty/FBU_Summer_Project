package me.caelumterrae.fbunewsapp.handlers.database;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.activities.SwipeActivity;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;
import me.caelumterrae.fbunewsapp.utility.Keyboard;


// This handler gets called in: LoginActivity login button handler
// This handler: Creates Current User & moves user to timeline intent with UID packaged inside
public class LoginHandler extends JsonHttpResponseHandler {

    private Context context;
    private Activity activity;
    private ImageView splash;

    public LoginHandler(Context context, Activity activity, ImageView splash) {
        this.context = context;
        this.activity = activity;
        this.splash = splash;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            int UID = response.getInt("UID");
            Log.e("uid",String.valueOf(UID));
            if (UID != -1) {
                User user = User.fromJSON(response);
                // Create the master user and start next intent to Swipeactivity
                showSplashScreen();
                CurrentUser.createUser(user, context, SwipeActivity.class);
            }
            else {
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

    private void showSplashScreen() {
        Keyboard.hideSoftKeyboard(activity);
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(new AlphaAnimation(0.0F, 1.0F));
        animation.addAnimation(new ScaleAnimation(0.8f, 1, 0.8f, 1)); // Change args as desired
        animation.setDuration(300);
        splash.startAnimation(animation);
        splash.setVisibility(View.VISIBLE);
    }
}