package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Semaphore;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.activities.DetailsActivity;

// This handler gets called in: DetailsActivity in oncreate
// This handler: will adjust the upvote button to have the background of the drawable
// based on whether the user previously liked the post
public class GetLikeHandler extends JsonHttpResponseHandler {

    Button button;
    Drawable drawable;
    JSONObject liked;
    public static String LIKED_KEY = "liked";
    public static String SEMAPHORE_KEY = "semaphore";
    Semaphore semaphore;
    Context context;

    public GetLikeHandler(Button button, Drawable drawable, Context context) throws JSONException {
        this.button = button;
        this.drawable = drawable;
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        boolean isLiked = false;
        try {
            isLiked = response.getBoolean("isLiked");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (isLiked) {
            button.setBackground(drawable);
            Log.i("GetLikeHandler", "User has liked this post before");
        } else {
            Log.i("GetLikeHandler", "User has NOT liked this post before");
        }

        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("isLiked", isLiked);
        intent.putExtra("source", "GetLikeHandler");
        context.startActivity(intent);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetLikeHandler","failure");
    }
}