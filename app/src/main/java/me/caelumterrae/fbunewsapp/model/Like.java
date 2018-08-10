package me.caelumterrae.fbunewsapp.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class Like {


    String articleUrl;
    String imageUrl;
    String articleTitle;

    //for Parceler if used
    public Like(){

    }

    public static Like fromJSON(JSONObject jsonObject) throws JSONException, ParseException {
        Log.e("Entering JSON formatter", "entered");
        Like like = new Like();
        like.setArticleTitle(jsonObject.getString("articleTitle"));
        Log.e("Article Title",jsonObject.getString("articleTitle"));
        like.setArticleUrl(jsonObject.getString("url"));
        like.setImageUrl(jsonObject.getString("mediaImage"));
        return like;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

}
