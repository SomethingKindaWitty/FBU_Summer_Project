package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Button;

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
// This handler: will adjust the upvote button to have the background of the drawable
// based on whether the user previously liked the post
public class AddRemoveLikeHandler extends JsonHttpResponseHandler {

    boolean isLiked;
    Drawable liked;
    Drawable defaultDrawable;
    Semaphore semaphore;

    public AddRemoveLikeHandler (boolean isLiked,
                                 Drawable defaultDrawable, Drawable liked, Semaphore semaphore) {
        this.isLiked = isLiked;
        this.defaultDrawable = defaultDrawable;
        this.liked = liked;
        this.semaphore = semaphore;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("AddRemoveLikeHandler", "Successful response");
                try {
                    if (isLiked) {
                        Log.e("GetLikeHandler", "User has just liked the post");
                    } else {
                        Log.e("GetLikeHandler", "User chose to un-like this post");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                semaphore.release();
            }
        }).start();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetLikeHandler","failure");
        semaphore.release();
    }
}