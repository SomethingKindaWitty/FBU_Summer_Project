package me.caelumterrae.fbunewsapp.model;

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
    private String mediaImage;
    private String articleTitle;

    private String userUrl;

    public static Comment fromJSON(JSONObject jsonObject) throws JSONException, ParseException {
        Comment comment = new Comment();
        comment.setDate(DateFunctions.getRelativeDateComment(jsonObject.getString("createdAt")));
        comment.setUrl(jsonObject.getString("articleUrl"));
        comment.setComment(jsonObject.getString("body"));
        if (jsonObject.has("username")) {
            comment.setUsername(jsonObject.getString("username"));
        }
        comment.setUid(jsonObject.getInt("uid"));
        if(jsonObject.has("profileImage")) {
            comment.setUserUrl(jsonObject.getString("profileImage"));
        }
        if(jsonObject.has("mediaImage")){
            comment.setMediaImage(jsonObject.getString("mediaImage"));
        }

        if(jsonObject.has("articleTitle")){
            comment.setArticleTitle(jsonObject.getString("articleTitle"));
        }

        return comment;
    }

    public Comment(){
        //For Parcel
    }

    public String getMediaImage() {
        return mediaImage;
    }

    public void setMediaImage(String mediaImage) {
        this.mediaImage = mediaImage;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
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

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }
}
