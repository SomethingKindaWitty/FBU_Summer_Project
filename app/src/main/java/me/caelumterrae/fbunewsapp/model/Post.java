package me.caelumterrae.fbunewsapp.model;

import android.widget.ProgressBar;

import java.util.List;

public class Post {
    private String title;
    private String imageUrl;
    private String body;
    private List<Post> relatedPosts;
    private int politicalBias; // 0 to 100, 0 = liberal, 100 = conservative.
    String date;
    //for Parceler if used
    public Post(){
        
    }

    public String getTitle(){
        return title;
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

    public String getBody(int lim){
        if (body.length() < lim){
            return body;
        }else {
            return body.substring(0, lim);
        }
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
}
