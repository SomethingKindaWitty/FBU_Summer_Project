package me.caelumterrae.fbunewsapp.handlers.database.comments;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

// This handler gets called in User Fragment in getNumComments()
// This handler: puts the number of comments posted into the List
public class GetNumCommentsHandler extends JsonHttpResponseHandler {

    Context context;
    List<Integer> numComments;

    public GetNumCommentsHandler(Context context, List<Integer> numComments) {
        this.context = context;
        this.numComments = numComments;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        int num = 0;
        try {
            num = response.getInt("num");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        numComments.clear();
        numComments.add(num);
        Log.e("num after", Integer.toString(numComments.get(0)));
        Log.e("NumComments", "success");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("NumComments", "failure");
    }

}
