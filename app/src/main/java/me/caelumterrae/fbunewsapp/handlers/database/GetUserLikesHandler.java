package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.adapters.LikesAdapter;
import me.caelumterrae.fbunewsapp.model.Like;
import me.caelumterrae.fbunewsapp.model.User;

public class GetUserLikesHandler extends JsonHttpResponseHandler {

    Context context;
    ArrayList<Like> likes;
    LikesAdapter adapter;
    RecyclerView rv;

    public GetUserLikesHandler(Context context, ArrayList<Like> likes, LikesAdapter adapter, RecyclerView rv) {
        this.context = context;
        this.likes = likes;
        this.adapter = adapter;
        this.rv = rv;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
        Log.e("UserLikes", "received");
        likes.clear();
        adapter.clear();
        for (int i = 0; i < response.length(); i ++) {
            try {
                Like like = Like.fromJSON(response.getJSONObject(i));
                Log.e("Title", like.getArticleTitle());
                likes.add(0, like);
                adapter.notifyItemInserted(0);
                rv.stopScroll();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetUserLikesHandler","failure");
        Log.e("GetUserLikesHandler", throwable.toString());
    }

}
