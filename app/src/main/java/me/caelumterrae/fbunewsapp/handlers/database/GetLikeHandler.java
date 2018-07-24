package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

// This handler gets called in: DetailsActivity in oncreate
// This handler: will adjust the upvote button to have the background of the drawable
// based on whether the user previously liked the post
public class GetLikeHandler extends JsonHttpResponseHandler {

    Button button;
    Drawable drawable;

    public GetLikeHandler(Button button, Drawable drawable) {
        this.button = button;
        this.drawable = drawable;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            boolean isLiked = response.getBoolean("isLiked");
            if (isLiked) {
                button.setBackground(drawable);
                Log.i("GetLikeHandler", "User has liked this post before");
            }
            else Log.i("GetLikeHandler", "User has NOT liked this post before");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetLikeHandler","failure");
    }
}