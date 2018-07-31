package me.caelumterrae.fbunewsapp.handlers.database.comments;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.activities.PoliticalActivity;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

// This handler gets called in: CommentActivity
// This handler: Toasts if comment is successfully posted or failed to post
public class PostCommentHandler extends JsonHttpResponseHandler  {

    Context context;
    public PostCommentHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Toast.makeText(context, "Comment posted successfully", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Toast.makeText(context, "Comment failed to post", Toast.LENGTH_LONG).show();
    }


}
