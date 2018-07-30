package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.concurrent.Semaphore;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.activities.DetailsActivity;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.model.User;

// This handler gets called in: DetailsActivity in button handler
// This handler: will adjust the upvote button to be selected
public class AddRemoveLikeHandler extends JsonHttpResponseHandler {

    boolean isLiked;
    Semaphore semaphore;
    ImageButton button;

    public AddRemoveLikeHandler (boolean isLiked, ImageButton button) {
        this.isLiked = isLiked;
        this.button = button;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.e("AddRemoveLikeHandler", "Successful response");

        // new Handler uses main thread to update UI || allows us to process each click linearly
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isLiked) {
                        button.setSelected(true);
                        Log.e("GetLikeHandler", "User has just liked the post");
                    } else {
                        button.setSelected(false);
                        Log.e("GetLikeHandler", "User chose to un-like this post");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetLikeHandler","failure");
        semaphore.release();
    }
}