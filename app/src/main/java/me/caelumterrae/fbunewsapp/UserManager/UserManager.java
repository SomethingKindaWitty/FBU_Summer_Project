package me.caelumterrae.fbunewsapp.UserManager;

import org.json.JSONException;

import java.util.concurrent.CountDownLatch;

import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.database.GetUserHandler;
import me.caelumterrae.fbunewsapp.model.User;

// Class that manages the current user (a cache'd copy).
// Purpose is to allow other classes and fragments to seamlessly access and modify our centralized user
public class UserManager {

    private static User myUser;
    private static Object lock = new Object();
    private static CountDownLatch latch  = new CountDownLatch(1);
    private ParseNewsClient parseNewsClient;
    // lock prevents us from accessing and modifying the user object at the same time


    // Creates and populates a user given a UID
    public UserManager(int uid) throws JSONException {
        synchronized (lock) {
            myUser = new User();
            parseNewsClient.getUser(uid, new GetUserHandler(myUser, latch));
        }
    }

    public static User getUser() throws InterruptedException {
        synchronized (lock) {
            latch.await();
            return myUser;
        }
    }

    public static void refreshUser() {
        // network call?
    }

    public static void setUser(User user) {
        synchronized (lock) {
            myUser = user;
        }
    }
}
