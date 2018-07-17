package me.caelumterrae.fbunewsapp.model;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Parcel
public class Post {

  
    String title;
    String imageUrl;
    String body;
    String summary;
    List<Post> relatedPosts;
    int politicalBias; // 0 to 100, 0 = liberal, 100 = conservative.
    String date;
    String url;
  
    //for Parceler if used
    public Post(){

    }

    public static Post fromJSON(JSONObject jsonObject) throws JSONException{
        Post post = new Post();
        
        String tempTime = jsonObject.getString("publishedAt");
        int index = tempTime.indexOf("T");
        post.setDate(tempTime.substring(0,index));

        post.setImageUrl(jsonObject.getString("urlToImage"));
        post.setSummary(jsonObject.getString("description"));
        post.setTitle(jsonObject.getString("title"));
        post.setUrl(jsonObject.getString("url"));
        post.setPoliticalBias(0);
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

    public List<Post> getRelatedPosts(){
        return relatedPosts;
    }

    public void setRelatedPosts(List<Post> posts){
        relatedPosts = posts;
    }

    public int getPoliticalBias() {
        return politicalBias;
    }

    public void setPoliticalBias(int politicalBias) {
        this.politicalBias = politicalBias;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

}
