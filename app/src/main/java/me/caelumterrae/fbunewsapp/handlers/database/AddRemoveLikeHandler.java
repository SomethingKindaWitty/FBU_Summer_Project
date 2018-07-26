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

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.activities.DetailsActivity;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.model.User;

// This handler gets called in: DetailsActivity in button handler
// This handler: will adjust the upvote button to have the background of the drawable
// based on whether the user previously liked the post
public class AddRemoveLikeHandler extends JsonHttpResponseHandler {

    boolean isLiked;
    Button button;
    Drawable drawable;
    Drawable defaultDrawable;
    Context context;
    Post post;
    int uid;

    public AddRemoveLikeHandler (Post post, int uid, boolean isLiked, Context context) {
        this.isLiked = isLiked;
        this.context = context;
        this.post = post;
        this.uid = uid;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.e("AddRemoveLikeHandler" ,"Successful response");
        try {
//            boolean isLiked = response.getBoolean("isLiked");
            if (isLiked) {
                Log.e("GetLikeHandler", "User has just liked the post");
            }
            else {
                Log.e("GetLikeHandler", "User chose to un-like this post");
            }
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
            intent.putExtra(User.class.getSimpleName(), uid);
            intent.putExtra("source", "AddRemoveLikeHandler");
            intent.putExtra("isLiked", isLiked);
            context.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetLikeHandler","failure");
    }
}