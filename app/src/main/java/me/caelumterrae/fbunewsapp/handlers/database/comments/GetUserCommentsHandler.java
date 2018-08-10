package me.caelumterrae.fbunewsapp.handlers.database.comments;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.adapters.CommentPostAdapter;
import me.caelumterrae.fbunewsapp.model.Comment;


// This handler gets called in User Fragment's CommentFragment
// This handler: puts the number of comments posted by a user into the list and notifies the adapter
public class GetUserCommentsHandler extends JsonHttpResponseHandler {

    Context context;
    CommentPostAdapter commentAdapter;
    ArrayList<Comment> comments;

    public GetUserCommentsHandler(Context context, ArrayList<Comment> comments, CommentPostAdapter commentAdapter) {
        this.context = context;
        this.comments = comments;
        this.commentAdapter = commentAdapter;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        Log.i("MADE IT HERE", "MADEITHERE");
        comments.clear();
        for(int i = 0; i < response.length(); i++){
            try {
                comments.add(0, Comment.fromJSON(response.getJSONObject(i)));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        commentAdapter.notifyDataSetChanged();
        super.onSuccess(statusCode, headers, response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
    }
}
