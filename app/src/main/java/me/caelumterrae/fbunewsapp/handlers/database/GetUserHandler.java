package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.activities.SwipeActivity;
import me.caelumterrae.fbunewsapp.model.User;

// This handler gets called in CurrentUser in both the create and refresh user methods
// This handler: refreshes the user object and may or may not move to next class afterwards
public class GetUserHandler extends JsonHttpResponseHandler {


    User user;
    Class<?> nextClass;
    Context context;

    // Refreshes/updates user object. If nextClass if valid, will bring the user to nextClass when done
    public GetUserHandler(User user, Context context, Class<?> nextClass) {
        this.user = user;
        this.context = context;
        this.nextClass = nextClass;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
        Log.e("GetUserHandler", "Got a response");
        try {
            User.fromJSON(user, response); // [!!] see here for converting response -> User
            if (nextClass != null) {
                Intent intent = new Intent(context, nextClass);
                context.startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetUserHandler","failure");
        Log.e("GetUserHandler", throwable.toString());
    }

}
