package me.caelumterrae.fbunewsapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.parceler.Parcel;

@Entity
@Parcel
public class Comment {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "UserID")
    private int uid;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "comment")
    private String comment;

    private String date;

    public Comment(){
        //For Parcel
    }

    public int getId() {
        return id;
    }

    public void setId(int uid) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
