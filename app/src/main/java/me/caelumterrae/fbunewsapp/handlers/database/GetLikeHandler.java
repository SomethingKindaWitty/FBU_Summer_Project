package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


// This handler gets called in: DetailsActivity in onCreate
// This handler: will adjust the upvote button to be selected if user previously liked it
public class GetLikeHandler extends JsonHttpResponseHandler {

    ImageButton button;
    Context context;

    public GetLikeHandler(ImageButton button) throws JSONException {
        this.button = button;
        this.context = context;
        Log.e("GetLikeHandler", "instantiated");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
        Log.e("GetLikeHandler", "response received");

        // New Handler lets us update UI w/ main thread
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (response.getBoolean("isLiked")) {
                        button.setSelected(true);
                        Log.e("GetLikeHandler", "User has liked this post before.");
                    } else {
                        Log.e("GetLikeHandler", "User has NOT liked this post before");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                button.setVisibility(View.VISIBLE); // Now lets users click on it now that we've
                // received a response. (Could result in error if user clicks on button before response received)
            }
        });
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetLikeHandler","failure");
    }
}