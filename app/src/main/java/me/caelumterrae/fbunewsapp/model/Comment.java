package me.caelumterrae.fbunewsapp.model;

//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;
import org.parceler.Parcel;

import java.text.ParseException;
import java.util.Date;

import me.caelumterrae.fbunewsapp.utility.DateFunctions;

@Parcel
public class Comment {

    private int uid;

    private String url;

    private String comment;

    private Date date;

    private String username;

    public static Comment fromJSON(JSONObject jsonObject) throws JSONException, ParseException {
        Comment comment = new Comment();
        comment.setDate(DateFunctions.getRelativeDateComment(jsonObject.getString("createdAt")));
        comment.setUrl(jsonObject.getString("articleUrl"));
        comment.setComment(jsonObject.getString("body"));
        comment.setUsername(jsonObject.getString("username"));
        comment.setUid(jsonObject.getInt("uid"));
        return comment;
    }

    public Comment(){
        //For Parcel
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRelativeTime() {
        PrettyTime p = new PrettyTime();
        return p.format(date);
    }
}
