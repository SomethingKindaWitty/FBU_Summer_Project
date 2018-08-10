package me.caelumterrae.fbunewsapp.handlers.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import me.caelumterrae.fbunewsapp.activities.PoliticalActivity;
import me.caelumterrae.fbunewsapp.activities.SwipeActivity;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;
import me.caelumterrae.fbunewsapp.utility.Keyboard;


// This handler gets called in: LoginActivity login button on click listener and signup button on click listener
// This handler: Creates Current User & moves user to timeline intent with UID packaged inside OR moves to political
// activity with username and password
public class LoginHandler extends JsonHttpResponseHandler {

    private Context context;
    private Activity activity;
    private ImageView splash;
    private Boolean bool;
    private String username;
    private String password;

    public LoginHandler(Boolean bool,String username, String password, Context context, Activity activity, ImageView splash) {
        this.context = context;
        this.activity = activity;
        this.splash = splash;
        this.bool = bool;
        this.username = username;
        this.password = password;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            int UID = response.getInt("UID");
            Log.e("uid",String.valueOf(UID));
            // If from login button
            if (bool) {
                if (UID != -1) {
                    User user = User.fromJSON(response);
                    // Create the master user and start next intent to Swipeactivity
                    showSplashScreen();
                    CurrentUser.createUser(user, context, SwipeActivity.class);
                }
                else {
                    Toast.makeText(context, "Invalid Login", Toast.LENGTH_LONG).show();
                }
            // If from signup button
            } else {
                if (UID != -1) {
                    Toast.makeText(context, "User already exists", Toast.LENGTH_LONG).show();
                }
                else {
                    // Create the master user and start next intent to PoliticalActivity
                    Intent i = new Intent(context, PoliticalActivity.class);
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    context.startActivity(i);
                }
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
        try {
            Keyboard.hideSoftKeyboard(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(new AlphaAnimation(0.0F, 1.0F));
        animation.addAnimation(new ScaleAnimation(0.8f, 1, 0.8f, 1)); // Change args as desired
        animation.setDuration(100);
        splash.startAnimation(animation);
        splash.setVisibility(View.VISIBLE);
        splash.bringToFront();
    }
}