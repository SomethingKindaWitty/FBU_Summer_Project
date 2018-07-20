package me.caelumterrae.fbunewsapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import me.caelumterrae.fbunewsapp.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();
//    private static UserDatabase INSTANCE;

    private static UserDatabase INSTANCE;


    //public abstract UserDao userDao();
    public static UserDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            UserDatabase.class, "UsersDatabase.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
