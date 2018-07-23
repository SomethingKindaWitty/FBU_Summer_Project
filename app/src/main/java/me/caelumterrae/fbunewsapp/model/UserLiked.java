package me.caelumterrae.fbunewsapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.parceler.Parcel;

@Entity
@Parcel
public class UserLiked {

    @NonNull
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "UserID")
    private int uid;

    @ColumnInfo(name = "url")
    private String url;

    public UserLiked(){
        //For Parcel
    }

    public int getId() {
        return id;
    }

    public void setId(int uid) {
        this.id = uid;
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







}
