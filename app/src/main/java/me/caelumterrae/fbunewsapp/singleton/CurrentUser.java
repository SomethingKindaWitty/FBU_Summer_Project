package me.caelumterrae.fbunewsapp.singleton;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.database.GetUserHandler;
import me.caelumterrae.fbunewsapp.model.User;

// Class that manages the current user (a cache'd copy).
// Purpose is to allow other classes and fragments to seamlessly access and modify our centralized user
// (No need to pass around anything in packages anymore)
public class CurrentUser {

    private static User myUser;
    private static Object lock = new Object();
    // lock prevents us from accessing and modifying the user object at the same time
    private static ParseNewsClient parseNewsClient;
    private static int myUid;
    private static Context myContext;
    private static User storedMyUser;

    // Creates and populates a user given a UID. When done, will bring you to nextClass
    // Implementation fixes concurrency issues where you try to get the user before it's created
    // (Called in Login and Signuphandler)
    public static void createUser(int uid, Context context, Class<?> nextClass) {
        synchronized (lock) {
            Log.e("CurrentUser", "Created User");
            myUser = new User();
            myUid = uid;
            myContext = context;
            parseNewsClient = new ParseNewsClient(context);
            try {
                parseNewsClient.getUser(myUid, new GetUserHandler(myUser, myContext, nextClass));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createUser(User user, Context context, Class<?> nextClass) {
        Log.e( "CurrentUser","User being created");
        myUid = user.getUid();
        myUser = user;
        parseNewsClient = new ParseNewsClient(myContext);
        myContext = context;
        Intent intent = new Intent(context, nextClass);
        context.startActivity(intent);
    }

    public static void setUid(int myUid) {
        CurrentUser.myUid = myUid;
    }

    public static User getUser() {
        synchronized (lock) {
            return myUser;
        }
    }

    public static int getUid() {
        return myUid;
    }


    public static void refreshUser() {
        // network call to refresh user
        synchronized (lock) {
            try {
                parseNewsClient.getUser(myUid, new GetUserHandler(myUser, myContext, null));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Used in CommentAdapter to store current user when we visit the OtherUserActivity (view other
    // people's profile).
    public static void storeCurrentUser() {
        Log.e("CurrentUser", "Stored User");
        storedMyUser = myUser;

    }

    // Used in OnBackPressed in OtherUserActivity to restore the current user
    public static void restoreCurrentUser() {
        Log.e("CurrentUser", "Restored User");
        myUser = storedMyUser;
    }

}
