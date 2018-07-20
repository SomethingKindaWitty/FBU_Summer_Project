package me.caelumterrae.fbunewsapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.parceler.Parcel;

@Entity
@Parcel
public class User {

    public User(){
        //for Parcel
    }

    @PrimaryKey
    int uid;

    @ColumnInfo(name = "username")
    String username;

    @ColumnInfo(name = "password")
    String password;

    @ColumnInfo(name = "profileImage")
    String profileUrl;

    //stored as a String separated by spaces due to database limitations
    @ColumnInfo(name = "categories")
    String categories;

    @ColumnInfo(name = "politicalPreference")
    int politicalPreference;

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
