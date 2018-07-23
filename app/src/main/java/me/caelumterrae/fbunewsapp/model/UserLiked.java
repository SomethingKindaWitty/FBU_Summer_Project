package me.caelumterrae.fbunewsapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.parceler.Parcel;

@Entity
@Parcel
public class UserLiked {

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
        return uid;
    }

    public void setId(int uid) {
        this.uid = uid;
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
