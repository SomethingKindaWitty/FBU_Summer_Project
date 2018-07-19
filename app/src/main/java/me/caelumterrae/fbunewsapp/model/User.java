package me.caelumterrae.fbunewsapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.parceler.Parcel;

@Entity
@Parcel
public class User {

    public User(){

    }
    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "profileImage")
    private String profileUrl;

    @ColumnInfo(name = "categories")
    private String categories;

    @ColumnInfo(name = "politicalPreference")
    private int politicalPreference;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getPoliticalPreference() {
        return politicalPreference;
    }

    public void setPoliticalPreference(int politicalPreference) {
        this.politicalPreference = politicalPreference;
    }

}
