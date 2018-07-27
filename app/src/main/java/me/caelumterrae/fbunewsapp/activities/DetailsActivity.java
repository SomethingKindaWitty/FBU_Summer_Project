package me.caelumterrae.fbunewsapp.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.RelatedAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.handlers.NewsDataHandler;
import me.caelumterrae.fbunewsapp.handlers.database.AddRemoveLikeHandler;
import me.caelumterrae.fbunewsapp.handlers.database.GetLikeHandler;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.model.User;

public class DetailsActivity extends AppCompatActivity {

    RecyclerView rvRelated;
    TextView tvTitle;
    TextView tvBody;
    ImageView ivMedia;
    ImageButton upVote;
    Post post;
    Drawable main;
    Drawable liked;
    ProgressBar pb;
    int userID;
    ParseNewsClient parseNewsClient;
    Semaphore semaphore;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Populate the fields using an intent
        Bundle bundle = getIntent().getExtras();
        post = Parcels.unwrap(bundle.getParcelable(Post.class.getSimpleName()));
        userID = bundle.getInt(User.class.getSimpleName());
        Log.e("DetailsUid",Integer.toString(userID));

        upVote = findViewById(R.id.btnLike);
        tvTitle = findViewById(R.id.tvTitle);
        rvRelated = findViewById(R.id.rvRelated);
        tvBody = findViewById(R.id.tvBody);
        ivMedia = findViewById(R.id.ivMedia);
        upVote = findViewById(R.id.btnLike);
        pb = findViewById(R.id.progressBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ArrayList<Post> posts = post.getRelatedPosts();

        // Getting the related posts here.
        if (posts == null) posts = new ArrayList<Post>();
        final RelatedAdapter relatedAdapter = new RelatedAdapter(posts, userID);
        rvRelated.setLayoutManager(layoutManager);
        rvRelated.setAdapter(relatedAdapter);



        parseNewsClient = new ParseNewsClient(getApplicationContext());
        final TopNewsClient topNewsClient = new TopNewsClient(this);
        try {
            // This sets the upvote button to the drawable based on whether the user previously liked
            // the post or not
            parseNewsClient.getLike(userID, post.getUrl(), new GetLikeHandler(upVote));

            // Gets the body of the article
            parseNewsClient.getData(post.getUrl(), new NewsDataHandler(post.getUrl(), tvBody, relatedAdapter, posts, topNewsClient, pb, this));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Puts image, body, and title into post
        RequestOptions fitCenter = new RequestOptions().fitCenter();
        tvTitle.setText(post.getTitle());
        tvBody.setText(post.getBody());
        Glide.with(this)
                .load(post.getImageUrl())
                .apply(fitCenter)
                .into(ivMedia);

        semaphore = new Semaphore(1);
        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpvoteClick();
            }
        });
    }

    // We will wait until we are finished removing or adding like (semaphore.acquire()).
    // Main thread blocks until its done.
    private void onUpvoteClick() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (upVote.isSelected()) {
            try {
                parseNewsClient.removeLike(userID, post.getPoliticalBias(), post.getUrl(),
                        new AddRemoveLikeHandler(false, main, liked, semaphore));
                upVote.setSelected(false);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else  {
            try {
                parseNewsClient.addLike(userID, post.getPoliticalBias(), post.getUrl(),
                        new AddRemoveLikeHandler(true, main, liked, semaphore));
                upVote.setSelected(true);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, SwipeActivity.class);
        i.putExtra("source", "DetailsActivity");
        i.putExtra("uid", userID);
        startActivity(i);
        finish();
    }

}
