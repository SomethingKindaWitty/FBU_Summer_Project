package me.caelumterrae.fbunewsapp.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import me.caelumterrae.fbunewsapp.utility.DateFunctions;

@Parcel
public class Post {

  
    String title;
    String imageUrl;
    String body;
    String summary;
    ArrayList<Post> relatedPosts;
    int politicalBias; // 0 to 100, 0 = liberal, 100 = conservative.
    Date date;
    String url;
    boolean isUpvoted;
  
    //for Parceler if used
    public Post(){

    }

    public static Post fromJSON(JSONObject jsonObject) throws JSONException, ParseException {
        Post post = new Post();
        post.setDate(DateFunctions.getRelativeDate(jsonObject.getString("publishedAt")));
        post.setImageUrl(jsonObject.getString("urlToImage"));
        post.setSummary(jsonObject.getString("description"));
        post.setTitle(jsonObject.getString("title"));
        post.setUrl(jsonObject.getString("url"));
        post.setPoliticalBias(50);
        if (jsonObject.getString("description").equals("null")) {
            post.setSummary("");
        } else {
            post.setSummary(jsonObject.getString("description"));
        }
        return post;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle(int lim){
        if (title.length() < lim || lim == -1){
            return title;
        }else {
            return title.substring(0, lim);
        }
    }

    public void setTitle(String t){
        title = t;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String url){
        imageUrl = url;
    }

    public String getBody(int lim)
    {
        if (body.length() < lim){
            return body;
        }else {
            return body.substring(0, lim);
        }
    }

    public String getBody() {
        return body;
    }

    public void setBody(String b){
        body = b;
    }

    public ArrayList<Post> getRelatedPosts(){
        return relatedPosts;
    }

    public void setRelatedPosts(ArrayList<Post> posts){
        relatedPosts = posts;
    }

    // returns a number 0, 25, 50, 75, 100
    // corresponds to: [left (0), left-center (25), moderate (50), right-center (75), right (100)]
    public int getPoliticalBias() {
        return politicalBias;
    }

    public void setPoliticalBias(int politicalBias) {
        this.politicalBias = politicalBias;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSummary(int lim) {
        if (summary.length() < lim){
            return summary;
        }else {
            return summary.substring(0, lim);
        }
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRelativeTime() {
        PrettyTime p = new PrettyTime();
        return p.format(date);
    }

    public boolean getUpvoted() {
        return isUpvoted;
    }

    public void setUpvoted(boolean upvoted) {
        isUpvoted = upvoted;
    }
}
