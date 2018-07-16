package me.caelumterrae.fbunewsapp.model;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Post {
    private String title;
    private String url;
    private String imageUrl;
    private String body;
    private String summary;
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

    public String getSummary() {
        return summary;
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
