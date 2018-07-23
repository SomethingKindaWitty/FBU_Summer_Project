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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        database = UserDatabase.getInstance(getApplicationContext());
        randomSingleton = RandomSingleton.getInstance();
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
                }else{
                    upVoted = true;
                }
                synchronized (likeconfirmed) {
                    likeconfirmed.notify();
                    Log.e("Object", "Booped");
                }
            }
        }).start();

        synchronized (likeconfirmed) {
            try {
                likeconfirmed.wait();
                Log.e("Object", "Done waiting");
                if (upVoted) {
                    Log.e("Object", "Passed If");
                    upVote.setBackground(drawable);
                }
            } catch (InterruptedException e) {
                Log.e("Object", "We errored");
                e.printStackTrace();
            }
        }
        Log.e("Object", "Got out of syncr]hrnioze");


        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (upVoted){
                    updateFile(false, post.getPoliticalBias());
                    upVote.setBackground(main);
                    upVoted = false;
                    updateList(like);

                } else {
                    // change tint color!
                    updateFile(true, post.getPoliticalBias());
                    upVote.setBackground(drawable);
                    upVoted = true;
                    updateList(user.getUid());
                }
            }
        });
    }

    // update list - delete
    public void updateList(final UserLiked like) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //delete like from database
                synchronized (add_confirmed){
                    try {
                        synchronized (likeconfirmed){
                            try{
                                likeconfirmed.wait();
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        add_confirmed.wait();
                        //
                        database.likedDao().delete(like);
                        synchronized (delete_confirmed){
                            delete_confirmed.notify();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!upVoted){
                    synchronized (delete_confirmed){
                        delete_confirmed.notify();
                    }
                }
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

                synchronized (delete_confirmed){
                    try {
                        synchronized (likeconfirmed){
                            try{
                                likeconfirmed.wait();
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        delete_confirmed.wait();
                        //add to database
                        database.likedDao().insertUserLiked(userliked);
                        synchronized (add_confirmed){
                            add_confirmed.notify();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

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
