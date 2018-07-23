package me.caelumterrae.fbunewsapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import me.caelumterrae.fbunewsapp.model.Comment;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.model.UserLiked;

@Database(entities = {User.class, UserLiked.class, Comment.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract LikedDao likedDao();
    public abstract CommentDao commentDao();

    private static UserDatabase INSTANCE;

    //Singleton style; only one database on each device
    public static UserDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            UserDatabase.class, "UserDatabase3.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
