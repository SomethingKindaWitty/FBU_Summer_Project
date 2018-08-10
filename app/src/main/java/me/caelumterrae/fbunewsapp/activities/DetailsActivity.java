package me.caelumterrae.fbunewsapp.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.RelatedAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.handlers.NewsDataHandler;
import me.caelumterrae.fbunewsapp.handlers.database.AddRemoveLikeHandler;
import me.caelumterrae.fbunewsapp.handlers.database.GetLikeHandler;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class DetailsActivity extends AppCompatActivity {

    RecyclerView rvRelated;
    ScrollView scrollView;
    TextView tvTitle;
    TextView tvBody;
    ImageView ivMedia;
    ImageButton upVote;
    ImageButton share;
    Button commentButton;
    Post post;
    ProgressBar pb;
    int userID;
    ParseNewsClient parseNewsClient;

    // For swipe event
    double downX;
    double upX;
    double downY;
    double upY;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Populate the fields using an intent
        Bundle bundle = getIntent().getExtras();
        post = Parcels.unwrap(bundle.getParcelable(Post.class.getSimpleName()));

        userID = CurrentUser.getUid();
        Log.e("DetailsUid", Integer.toString(userID));

        scrollView = findViewById(R.id.scrollView);

        upVote = findViewById(R.id.btnLike);
        tvTitle = findViewById(R.id.tvTitle);
        rvRelated = findViewById(R.id.rvRelated);
        tvBody = findViewById(R.id.tvBody);
        ivMedia = findViewById(R.id.ivMedia);
        upVote = findViewById(R.id.btnLike);
        pb = findViewById(R.id.progressBar);
        commentButton = findViewById(R.id.commentButton);
        share = findViewById(R.id.btnShare);
        upVote.setVisibility(View.INVISIBLE); // Hide button until it loads in Getlikehandler


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

        if(post.getBody() != null){
            pb.setVisibility(View.INVISIBLE);
        }

        // Puts image, body, and title into post
        RequestOptions fitCenter = new RequestOptions().fitCenter();
        tvTitle.setText(post.getTitle());
        tvBody.setText(post.getBody());
        Glide.with(this)
                .load(post.getImageUrl())
                .apply(fitCenter)
                .into(ivMedia);

        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpvoteClick();
            }
        });
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCommentsClick();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareClick();
            }
        });


        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return onSwipe(motionEvent);
            }
        });
    }

    private void onUpvoteClick() {
        try {
            if (upVote.isSelected())
                parseNewsClient.removeLike(userID, post.getPoliticalBias(), post.getUrl(),
                        new AddRemoveLikeHandler(false, upVote));
            else
                parseNewsClient.addLike(userID, post.getPoliticalBias(), post.getUrl(),
                        new AddRemoveLikeHandler(true, upVote));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onCommentsClick() {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("URL", post.getUrl());
        startActivity(intent);
    }

    private void onShareClick() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, post.getUrl());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share Article URL"));
    }

    // Detects left or right swipe
    private boolean onSwipe(MotionEvent motionEvent) {
        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = motionEvent.getX();
                downY = motionEvent.getY();
                return scrollView.onTouchEvent(motionEvent);
            case MotionEvent.ACTION_UP:
                upX = motionEvent.getX();
                upY = motionEvent.getY();

                double delta = upX - downX;
                double deltaY = upY - downY;

                if (Math.abs(delta) >= 150 && Math.abs(deltaY) <= 50) {
                    if (delta >= 0 ) {
                        // Left to right swipe
                        // Copy back button functionality
                        DetailsActivity.super.onBackPressed();
                    }
                }
                return scrollView.onTouchEvent(motionEvent);
        }
        // Always go to default scroll view if not left to right swipe
        return scrollView.onTouchEvent(motionEvent);
    }

}
