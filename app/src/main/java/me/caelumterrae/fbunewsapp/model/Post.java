package me.caelumterrae.fbunewsapp.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Parcel
public class Post {

  
    String title;
    String imageUrl;
    String body;
    String summary;
    List<Post> relatedPosts;
    int politicalBias; // 0 to 100, 0 = liberal, 100 = conservative.
    Date date;
    String url;
  
    //for Parceler if used
    public Post(){

    }

    public static Post fromJSON(JSONObject jsonObject) throws JSONException, ParseException {
        Post post = new Post();
        
        String tempTime = jsonObject.getString("publishedAt");
        String example = "\"2018-07-17T17:35:01Z\"";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
//        int index = tempTime.indexOf("T");
//        post.setDate(tempTime.substring(0,index));
        Date date = format.parse(tempTime);
        post.setDate(date);

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

}
