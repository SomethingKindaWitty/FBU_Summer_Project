package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
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

// This handler gets called in: DetailsActivity in oncreate
// This handler: will adjust the upvote button to be selected if user previously liked it
public class GetLikeHandler extends JsonHttpResponseHandler {

    ImageButton button;

    public GetLikeHandler(ImageButton button) throws JSONException {
        this.button = button;
        Log.e("GetLikeHandler", "instantiated");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.e("GetLikeHandler", "response received");

        try {
            if (response.getBoolean("isLiked")) {
                button.setSelected(true);
                Log.e("GetLikeHandler", "User has liked this post before");
            } else {
                Log.e("GetLikeHandler", "User has NOT liked this post before");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetLikeHandler","failure");
    }
}