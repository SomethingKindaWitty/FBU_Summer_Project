package me.caelumterrae.fbunewsapp.handlers.database;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

// This handler gets called in: DetailsActivity in button handler
// This handler: will adjust the upvote button to have the background of the drawable
// based on whether the user previously liked the post
public class AddRemoveLikeHandler extends JsonHttpResponseHandler {

    boolean isLiked;
    Button button;
    Drawable drawable;
    Drawable defaultDrawable;

    public AddRemoveLikeHandler (Button button, Drawable drawable, Drawable
            defaultDrawable, boolean isLiked) {
        this.isLiked = isLiked;
        this.button = button;
        this.drawable = drawable;
        this.defaultDrawable = defaultDrawable;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            boolean isLiked = response.getBoolean("isLiked");
            if (isLiked) {
                button.setBackground(drawable);
                Log.i("GetLikeHandler", "User has just liked the post");
            }
            else {
                button.setBackground(defaultDrawable);
                Log.i("GetLikeHandler", "User chose to un-like this post");
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