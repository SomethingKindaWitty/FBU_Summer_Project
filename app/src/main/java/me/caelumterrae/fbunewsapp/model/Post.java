package me.caelumterrae.fbunewsapp.model;

import android.content.Context;
import android.content.Intent;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.activities.DetailsActivity;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.handlers.hexagon.SpecificRelatedHandler;
import me.caelumterrae.fbunewsapp.utility.DateFunctions;

@Parcel
public class Post {

  
    String title;
    String imageUrl;
    String body;
    String summary;
    ArrayList<Post> relatedPosts;
    ArrayList<String> keywords;
    int politicalBias; // 0 to 100, 0 = liberal, 100 = conservative.
    Date date;
    String url;
    boolean isUpvoted;
  
    //for Parceler if used
    public Post(){
        keywords = new ArrayList<String>();
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

    public void open(Context context, int userId){
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra(Post.class.getSimpleName(), Parcels.wrap(this));
        i.putExtra("source","Login");
        i.putExtra(User.class.getSimpleName(), userId);
        context.startActivity(i);
    }

    public void getKeywords(final ParseNewsClient client) throws UnsupportedEncodingException, JSONException {
        client.getKeywords(this, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray keywordsResponse = response.getJSONArray("keywords");
                    for(int i = 0; i < keywordsResponse.length(); i++){
                        keywords.add(keywordsResponse.getString(i));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getKeywords(final ParseNewsClient client, final TopNewsClient topNewsClient, final ArrayList<ArrayList<Post>> postmap, final int x, final int y) throws UnsupportedEncodingException, JSONException {
        client.getKeywords(this, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray keywordsResponse = response.getJSONArray("keywords");
                    for(int i = 0; i < keywordsResponse.length(); i++){
                        keywords.add(keywordsResponse.getString(i));
                    }
                    //Okay now i have the keywords, call generate post on the necessary places on the map by passing the proper values.
                    if(x < 5){
                        if(y>=7){
                            //look to the upper right (reference gavins diagram in his notebook)
                            //upper right quadrant of hexagons
                            generatePost(client,topNewsClient, postmap, x-1, y+1);
                            generatePost(client,topNewsClient, postmap, x-1, y);
                            generatePost(client,topNewsClient, postmap, x, y+1);
                        }else{
                            //lower right quadrant of hexagons, look to the upper left
                            generatePost(client,topNewsClient, postmap, x-1, y-1);
                            generatePost(client,topNewsClient, postmap, x-1, y);
                            generatePost(client,topNewsClient, postmap, x, y-1);
                        }
                    }else{
                        if(y>=7){
                            //upper left quadrant of hexagons, look to lower right
                            generatePost(client,topNewsClient, postmap, x+1, y+1);
                            generatePost(client,topNewsClient, postmap, x+1, y);
                            generatePost(client,topNewsClient, postmap, x, y+1);
                        }else{
                            //lower left quadrant of hexagons, look to upper right
                            generatePost(client,topNewsClient, postmap, x+1, y-1);
                            generatePost(client,topNewsClient, postmap, x+1, y);
                            generatePost(client,topNewsClient, postmap, x, y-1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public static void generatePost(ParseNewsClient parseclient, TopNewsClient client, ArrayList<ArrayList<Post>> postmap, int x, int y){

        if(x == 0 || x == 10 || y == 0 || y == 14){
            return;
        }
        
        ArrayList<String> keywords = new ArrayList<String>();
        if(x < 5){
            if(y>=7){
                //look to the lower left (reference gavins diagram in his notebook)
                //upper right quadrant of hexagons

                //I use the min to ensure we get only one word if it is there. If it isn't, then don't add anything.
                keywords.addAll(postmap.get(x+1).get(y-1).keywords.subList(0,Math.min(2,postmap.get(x+1).get(y-1).keywords.size())));
                keywords.addAll(postmap.get(x+1).get(y).keywords.subList(0,Math.min(2,postmap.get(x+1).get(y).keywords.size())));
                keywords.addAll(postmap.get(x).get(y-1).keywords.subList(0,Math.min(2,postmap.get(x).get(y-1).keywords.size())));

            }else{
                //lower right quadrant of hexagons
                keywords.addAll(postmap.get(x+1).get(y+1).keywords.subList(0,Math.min(2,postmap.get(x+1).get(y+1).keywords.size())));
                keywords.addAll(postmap.get(x+1).get(y).keywords.subList(0,Math.min(2,postmap.get(x+1).get(y).keywords.size())));
                keywords.addAll(postmap.get(x).get(y+1).keywords.subList(0,Math.min(2,postmap.get(x).get(y+1).keywords.size())));
            }
        }else{
            if(y>=7){
                //upper left quadrant of hexagons
                keywords.addAll(postmap.get(x-1).get(y-1).keywords.subList(0,Math.min(2,postmap.get(x-1).get(y-1).keywords.size())));
                keywords.addAll(postmap.get(x-1).get(y).keywords.subList(0,Math.min(2,postmap.get(x-1).get(y).keywords.size())));
                keywords.addAll(postmap.get(x).get(y-1).keywords.subList(0,Math.min(2,postmap.get(x).get(y-1).keywords.size())));
            }else{
                //lower left quadrant of hexagons
                keywords.addAll(postmap.get(x-1).get(y+1).keywords.subList(0,Math.min(2,postmap.get(x-1).get(y+1).keywords.size())));
                keywords.addAll(postmap.get(x-1).get(y).keywords.subList(0,Math.min(2,postmap.get(x-1).get(y).keywords.size())));
                keywords.addAll(postmap.get(x).get(y+1).keywords.subList(0,Math.min(2,postmap.get(x).get(y+1).keywords.size())));
            }
        }
        //after getting the keywords from the posts, generate a post using a new endpoint that I have to create shoot.
        client.getSpecificRelated(keywords, new SpecificRelatedHandler(postmap, client.sourceBias, parseclient, client, x, y));
    }
}
