package me.caelumterrae.fbunewsapp.UserManager;

import me.caelumterrae.fbunewsapp.model.User;

// Class that manages the current user (a cache'd copy).
// Purpose is to allow other classes and fragments to seamlessly access and modify our centralized user
public class UserManager {

    private static User myUser;
    private static Object lock = new Object();
    // lock prevents us from accessing and modifying the user object at the same time

    public static User getUser() {
        synchronized (lock) {
            return myUser;
        }
    }

    public static void setUser(User user) {
        synchronized (lock) {
            myUser = user;
        }
    }
}
