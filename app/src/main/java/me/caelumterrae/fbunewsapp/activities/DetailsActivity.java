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
import java.util.List;
import java.util.concurrent.Semaphore;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.database.UserDatabase;
import me.caelumterrae.fbunewsapp.file.PoliticalAffData;
import me.caelumterrae.fbunewsapp.handlers.NewsDataHandler;
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
    Button upVote;
    Post post;
    Drawable main;
    Drawable drawable;
    ProgressBar pb;
    User user;
    UserLiked like;
    Boolean upVoted;
    UserDatabase database;
    List<UserLiked> userLikeds;
    Object likeconfirmed = "yay";
    Object add_confirmed = "yay";
    Object delete_confirmed = "yay";
    RandomSingleton randomSingleton;
    Semaphore waitForQueryingDatabase;
    Semaphore waitForButton;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        database = UserDatabase.getInstance(getApplicationContext());
        randomSingleton = RandomSingleton.getInstance();
        waitForQueryingDatabase = new Semaphore(0);
        waitForButton = new Semaphore(0);

        if (database == null) {
            Log.e("Database", "failed to create");
        } else {
            Log.e("Database", "created");
        }

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

        final RelatedAdapter relatedAdapter = new RelatedAdapter(posts, user);
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
        upVote.setBackground(main);

        // see if the like/upvote exists in the database
        new Thread(new Runnable() {
            @Override
            public void run() {
                like = database.likedDao().findLiked(user.getUid(), post.getUrl());
                if (like == null){
                    upVoted = false;
                    Log.e("Object", "Making userliked object");
                    like = new UserLiked();
                    like.setId(randomSingleton.nextInt());
                    like.setUid(user.getUid());
                    like.setUrl(post.getUrl());
                }else{
                    upVoted = true;
                }
                waitForQueryingDatabase.release();
                waitForButton.release();
            }
        }).start();

        try {
            waitForQueryingDatabase.acquire();
            Log.e("Object", "Done waiting");
            if (upVoted) {
                Log.e("Object", "Passed If");
                upVote.setBackground(drawable);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("Object", "Got out of semaphore");

        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.e("Object", "Waiting for button");
                    waitForButton.acquire();
                    Log.e("Object", "Got out of button semaphore and about to update it");
                    if (upVoted){
                        Log.e("Object", "About to update file");
                        updateFile(false, post.getPoliticalBias());
                        Log.e("Object", "Finished updating file");
                        upVote.setBackground(main);
                        upVoted = false;
                        updateList(like, user.getUid(), false);
                    } else {
                        // change tint color!
                        Log.e("Object", "About to update file");
                        updateFile(true, post.getPoliticalBias());
                        Log.e("Object", "Finished updating file");
                        upVote.setBackground(drawable);
                        Log.e("Object", "Done updating background");
                        upVoted = true;
                        Log.e("Object", "Before update list");
                        updateList(like, user.getUid(), true);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void updateList(final UserLiked like, final int userID, final Boolean adding){
        Log.e("Object", "Going into updateList body");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (adding) {
                    //create a new usercliked object
                    Log.e("Object", "About to insertUserLiked");
                    database.likedDao().insertUserLiked(like);
                } else {
                    //delete like from database
                    Log.e("Object", "about to Delete");
                    database.likedDao().delete(like);
                }
                Log.e("Object", "Finished updating button, releasing semaphore");
                waitForButton.release();
            }
        }).start();
    }

    // update list - delete
    public void updateList(final UserLiked like) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //delete like from database
                database.likedDao().delete(like);
            }
        }).start();
    }

    //update list - add
    public void updateList(final int userID){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //create a new userliked object
                UserLiked userliked = new UserLiked();
                userliked.setId(randomSingleton.nextInt());
                userliked.setUid(userID);
                userliked.setUrl(post.getUrl());

                database.likedDao().insertUserLiked(userliked);
                }
            }).start();
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
