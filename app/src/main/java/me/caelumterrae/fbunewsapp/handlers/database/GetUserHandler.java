package me.caelumterrae.fbunewsapp.handlers.database;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.activities.SwipeActivity;
import me.caelumterrae.fbunewsapp.model.User;

/* This handler gets called in: UserFragment, and possibly in comments
 * This handler: will pass back a user object and signal the semaphore that it finished
 * populating the user object.
 * Semaphore functions. .release(); .acquire();
 *
 * Usage: Instantiate a semaphore like so:
 * >> Semaphore mySemaphore = new Semaphore(0);
 * Package the semaphore inside an object like so:
 * >> JSONObject semaphoreObj = new JSONObject();
 * >> semaphoreObj.put(GetUserHandler.semaphoreKey, mySemaphore);
 * Then call:
 * >> parseNewsClient.getUser(uid, new GetUserHandler(myJsonUserObject, semaphoreObj);
 * Then wait for parseNewClient to populate myJsonUserObject by writing:
 * >> mySemaphore.acquire(); // this waits for semaphore.release to be called in line 54
 * Now, we are guaranteed to have myJsonUserObject populated, so we can access the user object with
 * >> User user = myJsonUserObject.get(GetUserHandler.userKey);
 *
 * :)
 *
 */

public class GetUserHandler extends JsonHttpResponseHandler {

    public static String USER_KEY = "user";
    public static String SEMAPHORE_KEY = "semaphore";

//    JSONObject userObject;
//    Semaphore semaphore;
    Context context;

//    public GetUserHandler(JSONObject userObject, JSONObject semaphoreObject) throws JSONException {
//        this.userObject = userObject;
//        this.semaphore = (Semaphore) semaphoreObject.get(SEMAPHORE_KEY);
//        Log.e("UserHandler", "Instantiated");
//    }

    public GetUserHandler(Context context) throws JSONException {
        this.context = context;
        Log.e("UserHandler", "Instantiated");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            User user = User.fromJSON(response); // [!!] see here for converting response -> User
            Log.e("Username", user.getUsername());
            Intent i = new Intent(context, SwipeActivity.class);
            i.putExtra("User", Parcels.wrap(user));
            i.putExtra("source","User");
            context.startActivity(i);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetUserHandler","failure");
        Log.e("GetUserHandler", throwable.toString());
    }

//    @Override
//    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//        try {
//            Log.e("UserHandler", "Success");
//            User user = User.fromJSON(response);  // [!!] see here for converting response -> User
//            userObject.put(USER_KEY, user);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Log.e("UserHandler", "semaphore is getting released");
//        semaphore.release();
//    }
//
//    @Override
//    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//        Log.e("GetUserHandler","failure");
//        semaphore.release();
//    }

}
