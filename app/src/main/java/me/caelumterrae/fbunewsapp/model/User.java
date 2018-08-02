package me.caelumterrae.fbunewsapp.model;

//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;
import org.parceler.Parcel;

import java.text.ParseException;

import me.caelumterrae.fbunewsapp.utility.DateFunctions;

//@Entity
@Parcel
public class User {

    public User(){
        //for Parcel
    }

    // Populates everything from JSON except categories
    // Categories and url are flipped temporarily TODO- edit backend to change flip
    public static void fromJSON(User user, JSONObject jsonObject) throws JSONException {
        user.setUid(jsonObject.getInt("UID"));
        user.setUsername(jsonObject.getString("username"));
        user.setPassword("password");
        user.setProfileUrl(jsonObject.getString("categories"));
        user.setPoliticalPreference(jsonObject.getDouble("politicalPreference"));
        user.setNumUpvoted(jsonObject.getInt("numUpvoted"));
        user.setCategories("url");
    }

//    @PrimaryKey
    int uid;
//
//    @ColumnInfo(name = "username")
    String username;
//
//    @ColumnInfo(name = "password")
    String password;

//    @ColumnInfo(name = "profileImage")
    String profileUrl;

    //stored as a String separated by spaces due to database limitations
//    @ColumnInfo(name = "categories")
    String categories;

//    @ColumnInfo(name = "politicalPreference")
    double politicalPreference;

//    @ColumnInfo(name = "numUpvoted")
    int numUpvoted;

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

    public double getPoliticalPreference() {
        return politicalPreference;
    }

    public void setPoliticalPreference(double politicalPreference) {
        this.politicalPreference = politicalPreference;
    }

    public int getNumUpvoted() {
        return numUpvoted;
    }

    public void setNumUpvoted(int numUpvoted) {
        this.numUpvoted = numUpvoted;
    }

}
