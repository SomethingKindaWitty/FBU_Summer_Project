package me.caelumterrae.fbunewsapp;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
        ArrayList<Post> posts = post.getRelatedPosts();
        // Getting the related posts here.
        if (posts == null) {
            posts = new ArrayList<Post>();
        }

        final RelatedAdapter relatedAdapter = new RelatedAdapter(posts);
        rvRelated.setLayoutManager(layoutManager);
        rvRelated.setAdapter(relatedAdapter);

        final TopNewsClient topNewsClient = new TopNewsClient();
        ParseNewsClient parseNewsClient = new ParseNewsClient(this);
        String test = post.getUrl();
        final ArrayList<Post> finalPosts = posts;
        try {
            parseNewsClient.getData(test, tvBody, relatedAdapter, finalPosts, topNewsClient);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
