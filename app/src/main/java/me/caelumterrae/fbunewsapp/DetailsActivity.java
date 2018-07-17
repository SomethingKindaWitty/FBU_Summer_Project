package me.caelumterrae.fbunewsapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;


import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.utilities.RelatedAdapter;

public class DetailsActivity extends AppCompatActivity {
    RecyclerView rvRelated;
    TextView tvTitle;
    TextView tvBody;
    ImageView ivMedia;
    Button upVote;
    Post post;
    Drawable main;
    Drawable drawable;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        // populate the fields using an intent
        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        tvTitle = findViewById(R.id.tvTitle);
        rvRelated = findViewById(R.id.rvRelated);
        tvBody = findViewById(R.id.tvBody);
        ivMedia = findViewById(R.id.ivMedia);
        upVote = findViewById(R.id.btnLike);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        List<Post> posts = post.getRelatedPosts();
        // Getting the related posts here.
        if (posts == null) {
            posts = new ArrayList<Post>();
        }

        final RelatedAdapter relatedAdapter = new RelatedAdapter(posts);
        rvRelated.setLayoutManager(layoutManager);
        rvRelated.setAdapter(relatedAdapter);

        final TopNewsClient topNewsClient = new TopNewsClient();
        ParseNewsClient parseNewsClient = new ParseNewsClient(this);
        final List<Post> finalPosts = posts;
        String test = post.getUrl();
        try {
            parseNewsClient.getData(test, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray keywords = response.getJSONArray("keywords");
                        String keyword = keywords.getString(0);
                        String text = response.getString("text");
                        tvBody.setText(text);

                        //TODO: update the trump keyword to be the keyword received from the call to our backend
                        topNewsClient.getRelatedNews(keyword, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                // parse the response to Post object
                                // add the Post object to the arraylist
                                try {
                                    JSONArray results = response.getJSONArray(TopNewsClient.ROOT_NODE);
                                    for (int i = 0; i < results.length(); i++) {
                                        Post post = Post.fromJSON(results.getJSONObject(i));
                                        finalPosts.add(post);
                                        // notify adapter that a row was added
                                        relatedAdapter.notifyItemInserted(finalPosts.size()-1); // latest item
                                    }
                                    Log.i("TopNewsClient", String.format("Loaded %s posts", results.length()));
                                } catch (JSONException e) {
                                    Log.e("TopNewsClient", "Failed to parse top posts", e);
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Log.e("TopNewsClient", "Failed to get data from now playing endpoint", throwable);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(DetailsActivity.this, "ayy", Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        RequestOptions cropOptions = new RequestOptions().centerCrop();
        RequestOptions roundedEdges = new RequestOptions().transform(new RoundedCornersTransformation(10, 10));
        RequestOptions fitCenter = new RequestOptions().fitCenter();


        tvTitle.setText(post.getTitle());
        tvBody.setText(post.getBody());
        Glide.with(this)
                .load(post.getImageUrl())
                .apply(fitCenter)
                .into(ivMedia);


        main = DrawableCompat.wrap(getDrawable(android.R.drawable.ic_menu_more));
        drawable = DrawableCompat.wrap(getDrawable(android.R.drawable.ic_menu_more));
        DrawableCompat.setTint(drawable, getResources().getColor(R.color.green));

        if (post.getUpvoted()){
            upVote.setBackground(drawable);
        }else{
            upVote.setBackground(main);
        }

    }
    //  Upvote Button Handler - Saves data from button and brings user to activity main
    public void onUpvote(View v) {
        if (post.getUpvoted()){
            post.setUpvoted(false);
            upVote.setBackground(main);
        } else {
            // change tint color!
            post.setUpvoted(true);
            upVote.setBackground(drawable);
        }
    }
}
