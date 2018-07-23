package me.caelumterrae.fbunewsapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.model.UserLiked;

@Dao
public interface LikedDao {

    //Queries for accessing database information
    //CANNOT be called on Main thread
    @Query("SELECT * FROM userliked")
    List<UserLiked> getAll();

    @Query("SELECT * FROM userliked WHERE UserID LIKE :uid")
    List<UserLiked> findByID(int uid);

    @Query("SELECT * FROM userliked WHERE UserID LIKE :uid AND url LIKE :url")
    UserLiked findLiked(int uid, String url);

    @Delete
    void delete(UserLiked userliked);

    @Insert
    void insertUser(UserLiked userliked);
}
