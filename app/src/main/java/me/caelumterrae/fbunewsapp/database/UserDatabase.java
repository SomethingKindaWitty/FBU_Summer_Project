package me.caelumterrae.fbunewsapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import me.caelumterrae.fbunewsapp.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    private static UserDatabase INSTANCE;
}
