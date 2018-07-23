package me.caelumterrae.fbunewsapp.activities;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.parceler.Parcels;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.file.PoliticalAffData;
import me.caelumterrae.fbunewsapp.handlers.NewsDataHandler;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.adapters.RelatedAdapter;
import me.caelumterrae.fbunewsapp.model.User;

public class DetailsActivity extends AppCompatActivity {

    RecyclerView rvRelated;
    TextView tvTitle;
    TextView tvBody;
    ImageView ivMedia;
    Button upVote;
    Post post;
    Drawable main;
    Drawable drawable;
    ProgressBar pb;
    User user;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        // populate the fields using an intent
        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        user = Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));

        tvTitle = findViewById(R.id.tvTitle);
        rvRelated = findViewById(R.id.rvRelated);
        tvBody = findViewById(R.id.tvBody);
        ivMedia = findViewById(R.id.ivMedia);
        upVote = findViewById(R.id.btnLike);
        pb = findViewById(R.id.progressBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ArrayList<Post> posts = post.getRelatedPosts();
        // Getting the related posts here.
        if (posts == null) {
            posts = new ArrayList<Post>();
        }

        final RelatedAdapter relatedAdapter = new RelatedAdapter(posts);
        rvRelated.setLayoutManager(layoutManager);
        rvRelated.setAdapter(relatedAdapter);

        final TopNewsClient topNewsClient = new TopNewsClient(this);
        ParseNewsClient parseNewsClient = new ParseNewsClient(this);
        String test = post.getUrl();
        try {
            parseNewsClient.getData(test, new NewsDataHandler(test, tvBody, relatedAdapter, posts, topNewsClient, pb, this));
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


        // TODO put the right icon (if it was upvoted before) on page load
        main = DrawableCompat.wrap(getDrawable(android.R.drawable.ic_menu_more));
        drawable = DrawableCompat.wrap(getDrawable(android.R.drawable.ic_menu_more));
        DrawableCompat.setTint(drawable, getResources().getColor(R.color.green));

        if (post.getUpvoted()) {
            upVote.setBackground(drawable);
        } else {
            upVote.setBackground(main);
        }

        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (post.getUpvoted()){
                    post.setUpvoted(false);
                    updateFile(false, post.getPoliticalBias());
                    upVote.setBackground(main);
                } else {
                    // change tint color!
                    post.setUpvoted(true);
                    updateFile(true, post.getPoliticalBias());
                    upVote.setBackground(drawable);
                }
            }
        });

    }
    //  Upvote Button Handler - Saves data from button and brings user to activity main
    public void onUpvote(View v) {
        if (post.getUpvoted()){
            post.setUpvoted(false);
            updateFile(false, post.getPoliticalBias());
            upVote.setBackground(main);
        } else {
            // change tint color!
            post.setUpvoted(true);
            updateFile(true, post.getPoliticalBias());
            upVote.setBackground(drawable);
        }
    }

    public void updateFile(boolean isUpvoting, int politicalBias) {
        int numVotes = user.getNumUpvoted();
        double voteAvg = user.getPoliticalPreference();
        Log.i("this", Integer.toString(numVotes) + " " + Double.toString(voteAvg));
        if (isUpvoting) {
            // increase num votes and calculate new average
            user.setNumUpvoted(numVotes+1);
            user.setPoliticalPreference((numVotes*voteAvg+politicalBias)/(numVotes+1));
        }
        else { // decrease num votes and calculate new average
            user.setNumUpvoted(numVotes-1);
            user.setPoliticalPreference((numVotes*voteAvg-politicalBias)/(numVotes-1));
        }
        Log.i("this", Integer.toString(user.getNumUpvoted()) + " " + Double.toString(user.getPoliticalPreference()));
    }

}
