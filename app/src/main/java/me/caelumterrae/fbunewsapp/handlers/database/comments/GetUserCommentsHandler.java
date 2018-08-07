package me.caelumterrae.fbunewsapp.handlers.database.comments;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.model.Comment;

public class GetUserCommentsHandler extends JsonHttpResponseHandler {

    Context context;
    ArrayList<Comment> comments;

    public GetUserCommentsHandler(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Toast.makeText(context, "Comment received successfully", Toast.LENGTH_LONG).show();
        // TODO- ask to change backend -- does not work yet
        JSONArray result = new JSONArray();
        try {
            result = response.getJSONArray("comments");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < response.length(); i++){
            try {
                comments.add(0, Comment.fromJSON(result.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Toast.makeText(context, "Comment failed to get", Toast.LENGTH_LONG).show();
    }
}
