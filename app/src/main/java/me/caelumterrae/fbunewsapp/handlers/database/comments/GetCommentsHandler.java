package me.caelumterrae.fbunewsapp.handlers.database.comments;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.adapters.CommentAdapter;
import me.caelumterrae.fbunewsapp.model.Comment;

// This handler gets called in: CommentActivity
// This handler: Adds response to comments arraylist and comments adapter
public class GetCommentsHandler extends JsonHttpResponseHandler {
    Context context;
    CommentAdapter commentAdapter;
    ArrayList<Comment> comments;

    public GetCommentsHandler(Context context, CommentAdapter commentAdapter, ArrayList<Comment> comments) {
        this.context = context;
        this.commentAdapter = commentAdapter;
        this.comments = comments;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray results) {
        Log.e("GetCommentsHandler", "Got a response of size " + Integer.toString(results.length()));
        try {
            // Prevents same comments from being added to the overall list
            comments.clear();
            commentAdapter.clear();
            for (int i = 0; i < results.length(); i++) {
                Log.e("GCH", results.getJSONObject(i).getString("body"));
                Comment comment = Comment.fromJSON(results.getJSONObject(i));
                comments.add(0, comment);
                commentAdapter.notifyItemInserted(0);
            }
            Log.e("GetCommentsHandler", String.format("Loaded %s comments", results.length()));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("GetCommentsHandler", "error", e);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("GetCommentsHandler", "parse error", e);
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetCommentsHandler", "failed to grab data from endpoint", throwable);
    }
}
