package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.concurrent.Semaphore;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.model.User;

/* This handler gets called in: UserFragment, and possibly in comments
 * This handler: will pass back a user object and signal the semaphore that it finished
 * populating the user object.
 * Semaphore functions. .release(); .acquire();
 *
 * Usage: Instantiate a semaphore like so:
 * >> Semaphore mySemaphore = new Semaphore(0);
 * Then call:
 * >> parseNewsClient.getUser(uid, new GetUserHandler(myJsonUserObject, mySemaphore);
 * Then wait for parseNewClient to populate myJsonUserObject by writing:
 * >> mySemaphore.acquire(); // this waits for semaphore.release to be called in line 54
 * Now, we are gurranteed to have myJsonUserObject populated, so we can access the user object with
 * User user = myJsonUserObject.get(GetUserHandler.userKey);
 *
 * :)
 *
 */

public class GetUserHandler extends JsonHttpResponseHandler {

    public static String userKey = "user";

    JSONObject userObject;
    Semaphore semaphore;

    public GetUserHandler(JSONObject userObject, Semaphore semaphore) {
        this.userObject = userObject;
        this.semaphore = semaphore;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            User user = User.fromJSON(response);  // [!!] see here for converting response -> User
            userObject.put(userKey, user);
            semaphore.release();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetLikeHandler","failure");
    }
}
