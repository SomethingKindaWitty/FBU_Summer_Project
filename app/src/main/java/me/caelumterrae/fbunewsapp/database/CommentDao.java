package me.caelumterrae.fbunewsapp.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import me.caelumterrae.fbunewsapp.model.Comment;
import me.caelumterrae.fbunewsapp.model.User;

@Dao
public interface CommentDao {
    //Queries for accessing database information
    //CANNOT be called on Main thread
    @Query("SELECT * FROM comment")
    List<Comment> getAll();

    @Query("SELECT * FROM comment WHERE userID LIKE :uid")
    Comment findByUserID(int uid);

    @Query("SELECT * FROM comment WHERE url LIKE :url")
    Comment findByURL(String url);

    @Insert
    void insertAll(Comment... comment);

    @Delete
    void delete(Comment comment);

    @Insert
    void insertComment(Comment comment);

}
