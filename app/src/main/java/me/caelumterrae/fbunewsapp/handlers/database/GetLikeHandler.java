package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

// This handler gets called in: DetailsActivity in oncreate
// This handler: will adjust the upvote button to have the background of the drawable
// based on whether the user previously liked the post
public class GetLikeHandler extends JsonHttpResponseHandler {

    ImageButton button;
    Drawable drawable;
    Post post;
    int uid;
//    JSONObject liked;
//    public static String LIKED_KEY = "liked";
//    public static String SEMAPHORE_KEY = "semaphore";
//    Semaphore semaphore;
    Context context;

    public GetLikeHandler(ImageButton button, Drawable drawable, Post post, int uid, Context context) throws JSONException {
        this.button = button;
        this.drawable = drawable;
        this.post = post;
        this.context = context;
        this.uid = uid;
        Log.e("GetLikeHandler", "instantiated");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.e("GetLikeHandler", "response received");
        boolean isLiked = false;
        try {
            isLiked = response.getBoolean("isLiked");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (isLiked) {
            button.setSelected(true);
            Log.e("GetLikeHandler", "User has liked this post before");
        } else {
            Log.e("GetLikeHandler", "User has NOT liked this post before");
        }
    }

//        Intent intent = new Intent(context, DetailsActivity.class);
//        intent.putExtra("isLiked", isLiked);
//        intent.putExtra("source", "GetLikeHandler");
//        intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
//        intent.putExtra(User.class.getSimpleName(),uid);
//        context.startActivity(intent);
//        Log.e("Intent","started");
//    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetLikeHandler","failure");
    }
}