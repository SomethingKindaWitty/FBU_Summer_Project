package me.caelumterrae.fbunewsapp.singleton;

import android.content.Context;

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

    // Creates and populates a user given a UID. When done, will bring you to nextClass
    // Implementation fixes concurrency issues where you try to get the user before it's created
    // (Called in Login and Signuphandler)
    public static void createUser(int uid, Context context, Class<?> nextClass) throws JSONException, UnsupportedEncodingException {
        synchronized (lock) {
            myUser = new User();
            myUid = uid;
            myContext = context;

            parseNewsClient = new ParseNewsClient(context);
            parseNewsClient.getUser(myUid, new GetUserHandler(myUser, myContext, nextClass));
        }
    }

    public static User getUser() throws InterruptedException {
        synchronized (lock) {
            return myUser;
        }
    }

    public static int getUid() {
        return myUid;
    }

    public static void refreshUser() throws JSONException, UnsupportedEncodingException {
        // network call to refresh user
        synchronized (lock) {
            parseNewsClient.getUser(myUid, new GetUserHandler(myUser, myContext, null));
        }
    }

}
