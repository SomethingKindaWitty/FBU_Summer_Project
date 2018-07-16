package me.caelumterrae.fbunewsapp.model;

import android.widget.ProgressBar;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Post {
    private String title;
    private String imageUrl;
    private String body;
    private List<Post> relatedPosts;

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


}
