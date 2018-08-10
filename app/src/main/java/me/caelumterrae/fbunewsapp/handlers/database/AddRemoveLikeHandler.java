package me.caelumterrae.fbunewsapp.handlers.database;

import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.concurrent.Semaphore;

import cz.msebera.android.httpclient.Header;

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