package me.caelumterrae.fbunewsapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;

import java.util.List;

import me.caelumterrae.fbunewsapp.model.User;

@Dao
public interface UserDao {


    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid LIKE :uid")
    User findByID(int uid);

    @Query("SELECT * FROM user WHERE username LIKE :username AND "
            + "password LIKE :password LIMIT 1")
    User findByName(String username, String password);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Insert
    void insertUser(User user);
}
