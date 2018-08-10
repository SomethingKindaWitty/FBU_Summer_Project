package me.caelumterrae.fbunewsapp.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.media.MediaPlayer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.UserTabsAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.CommentFragment;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.PoliticalAffiliationFragment;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.UpvotedFragment;
import me.caelumterrae.fbunewsapp.handlers.database.comments.GetNumCommentsHandler;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;
import me.caelumterrae.fbunewsapp.utility.DateFunctions;

public class OtherUserActivity extends AppCompatActivity {

    // All of the fragments that belong ot this fragment
    public UpvotedFragment upvotedFragment;
    public CommentFragment commentFragment;
    public TextView username;
    public ImageView profileImage;
    public TextView numUpvoted;
    public TextView numCommented;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SwipeRefreshLayout swipeContainer;
    MediaPlayer mediaPlayer;
    private User user;
    private ArrayList<Integer> num = new ArrayList<>(Arrays.asList(0));
    private ParseNewsClient parseNewsClient;
    ScrollView scrollView;

    // For swipe event
    double downX;
    double upX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);
        // Add the fragments to the list
        upvotedFragment = new UpvotedFragment();
        commentFragment = new CommentFragment();
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager, upvotedFragment, commentFragment);
        tabLayout.setupWithViewPager(viewPager);

        swipeContainer = findViewById(R.id.swipeContainer);
        scrollView = findViewById(R.id.scrollView);

        user = CurrentUser.getUser();

        if (user != null){
            getNumComments();
            Log.e("url", user.getProfileUrl());
            createUser(user.getUsername(), user.getNumUpvoted(), user.getProfileUrl(), num.get(0));
            //create our quacking refresh sound
            mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.quack);
            final MediaPlayer quack_sound = mediaPlayer;

            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    quack_sound.start();
                    getNumComments();
                    createUser(user.getUsername(), user.getNumUpvoted(), user.getProfileUrl(), num.get(0));
                    Log.e("url", user.getProfileUrl());

                    // Refreshes tabbed fragments
                    upvotedFragment.refresh();
                    commentFragment.refresh();

                    swipeContainer.setRefreshing(false);
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(R.color.duck_beak,
                    R.color.sea_blue, R.color.yellow_duck,
                    R.color.sea_blue_light);

            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return onSwipe(motionEvent);
                }
            });
        }

    }

    private void setupViewPager(ViewPager viewPager, UpvotedFragment u, CommentFragment c ) {
        UserTabsAdapter adapter = new UserTabsAdapter(getSupportFragmentManager());
        adapter.addFragment(u, "Upvoted");
        adapter.addFragment(c, "Comments");
        viewPager.setAdapter(adapter);
    }

    public void createUser(String name, int numVotes, String profileUrl, int numComments) {

        username = findViewById(R.id.name);
        profileImage = findViewById(R.id.profImage);
        numUpvoted = findViewById(R.id.numUpvoted);
        numCommented = findViewById(R.id.numComments);

        username.setText(name);
        numUpvoted.setText(String.valueOf(numVotes));
        numCommented.setText(String.valueOf(numComments));


        // Checks to see if the user has already set a profile image
        // and loads appropriate image
        if (!profileUrl.equals("null")) {
            Log.e("URL", profileUrl);
            Glide.with(getApplicationContext())
                    .load(profileUrl)
                    .apply(new RequestOptions().circleCrop().error(R.drawable.user))
                    .into(profileImage);
        }

    }

    // Sends a reference to an arraylist which will contain the number of comments a user
    // has posted to the client/handler
    public void getNumComments(){
        parseNewsClient = new ParseNewsClient(getApplicationContext());
        try {
            Log.e("num before", Integer.toString(num.get(0)));
            parseNewsClient.getNumComments(user.getUid(), new GetNumCommentsHandler(getApplicationContext(), num));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Detects left or right swipe
    private boolean onSwipe(MotionEvent motionEvent) {
        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = motionEvent.getX();
                return true;
            case MotionEvent.ACTION_UP:
                upX = motionEvent.getX();

                double delta = upX - downX;

                if (Math.abs(delta) >= 150) {
                    if (delta >= 0 ) {
                        // Left to right swipe
                        // Copy back button functionality
                        OtherUserActivity.super.onBackPressed();
                        CurrentUser.restoreCurrentUser();
                    }
                }
                return true;
        }

        return false;
    }

    // Restores "CurrentUser" To the actual user!
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CurrentUser.restoreCurrentUser();

    }
}
