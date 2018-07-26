package me.caelumterrae.fbunewsapp.activities;

import android.annotation.TargetApi;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.database.UserDatabase;
import me.caelumterrae.fbunewsapp.file.PoliticalAffData;
import me.caelumterrae.fbunewsapp.handlers.NewsDataHandler;
import me.caelumterrae.fbunewsapp.handlers.database.AddRemoveLikeHandler;
import me.caelumterrae.fbunewsapp.handlers.database.GetLikeHandler;
import me.caelumterrae.fbunewsapp.handlers.database.GetUserHandler;
import me.caelumterrae.fbunewsapp.handlers.database.LoginHandler;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.adapters.RelatedAdapter;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.model.UserLiked;
import me.caelumterrae.fbunewsapp.utility.RandomSingleton;

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
    Boolean upVoted;
    Boolean gotLike;
    ParseNewsClient parseNewsClient;
    Semaphore semaphore;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // populate the fields using an intent
        Bundle bundle = getIntent().getExtras();
        post = Parcels.unwrap(bundle.getParcelable(Post.class.getSimpleName()));
        userID = bundle.getInt(User.class.getSimpleName());

        upVote = findViewById(R.id.btnLike);
        // unliked and liked drawables
        main = DrawableCompat.wrap(getDrawable(android.R.drawable.ic_menu_more));
        liked = DrawableCompat.wrap(getDrawable(android.R.drawable.ic_menu_more));
        DrawableCompat.setTint(liked, getResources().getColor(R.color.green));

        parseNewsClient = new ParseNewsClient(getApplicationContext());

        String source = bundle.getString("source");

        if (source.contentEquals("Related Adapter") || source.contentEquals("PoliticalActivity")|| source.contentEquals("LoginActivity")) {
            try {
                // This sets the upvote button to the drawable based on whether the user previously liked
                // the post or not
                parseNewsClient.getLike(userID, post.getUrl(), new GetLikeHandler(upVote, liked, post, userID, getApplicationContext()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            upVoted = false;
            gotLike = false;
        } else {
            upVoted = bundle.getBoolean("isLiked");
            Log.e("DetailsActivity", "isLiked call successful");
            gotLike = true;
            if (upVoted) {
                upVote.setSelected(true);
            } else {
                upVote.setSelected(false);
            }
        }

//        // if the bundle was received from getLike, this boolean will be available
//        // else, a call must be made
//        try {
//            upVoted = bundle.getBoolean("isLiked");
//            Log.e("DetailsActivity/AddRemoveLikeHandler", "isLiked call successful");
//            gotLike = true;
//        } catch (Exception er) {
//            try {
//                // This sets the upvote button to the drawable based on whether the user previously liked
//                // the post or not
//                parseNewsClient.getLike(userID, post.getUrl(), new GetLikeHandler(upVote, liked, post, userID, getApplicationContext()));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            upVoted = false;
//            gotLike = false;
//        }

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

        final RelatedAdapter relatedAdapter = new RelatedAdapter(posts, userID);
        rvRelated.setLayoutManager(layoutManager);
        rvRelated.setAdapter(relatedAdapter);

        // gets the body of the article
        final TopNewsClient topNewsClient = new TopNewsClient(this);
        try {
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
                // release semaphore
                if (upVoted) {
                    //change background to main
                    upVote.setSelected(true);
                } else {
                    //change background to upVoted
                    upVote.setSelected(false);
                }
            }
        });
    }

    // We will wait until we are finished removing or adding like (semaphore.acquire()).
    // Once we are signalled that we are not removing or addding like, then we will make a new
    // thread to add or remove like.
    private void onUpvoteClick() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (upVoted) {
            try {
                parseNewsClient.removeLike(userID, post.getPoliticalBias(), post.getUrl(),
                        new AddRemoveLikeHandler(false, main, liked, semaphore));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else  {
            try {
                parseNewsClient.addLike(userID, post.getPoliticalBias(), post.getUrl(),
                        new AddRemoveLikeHandler(true, main, liked, semaphore));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        upVoted = !upVoted;
        // semaphore.release();


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
