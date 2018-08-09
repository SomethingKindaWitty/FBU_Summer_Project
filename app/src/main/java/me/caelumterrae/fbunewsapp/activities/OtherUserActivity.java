package me.caelumterrae.fbunewsapp.activities;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    public PoliticalAffiliationFragment politicalAffiliationFragment;
    public UpvotedFragment upvotedFragment;
    public CommentFragment commentFragment;
    public TextView username;
    public ImageView profileImage;
    public TextView politicalAffiliation;
    public TextView numUpvoted;
    public TextView numCommented;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SwipeRefreshLayout swipeContainer;
    MediaPlayer mediaPlayer;
    private User user;
    private ArrayList<Integer> num = new ArrayList<>(Arrays.asList(0));
    private ParseNewsClient parseNewsClient;
    DecimalFormat df = new DecimalFormat("#.#");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);
        // Add the fragments to the list
        politicalAffiliationFragment = new PoliticalAffiliationFragment();
        upvotedFragment = new UpvotedFragment();
        commentFragment = new CommentFragment();
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager, politicalAffiliationFragment,upvotedFragment,commentFragment);
        tabLayout.setupWithViewPager(viewPager);

        swipeContainer = findViewById(R.id.swipeContainer);

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
                    politicalAffiliationFragment.refresh();
                    upvotedFragment.refresh();
                    commentFragment.refresh();

                    swipeContainer.setRefreshing(false);
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(R.color.duck_beak,
                    R.color.sea_blue, R.color.yellow_duck,
                    R.color.sea_blue_light);
        }

    }


    private void setupViewPager(ViewPager viewPager, PoliticalAffiliationFragment p, UpvotedFragment u, CommentFragment c ) {
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


    // Restores "CurrentUser" To the actual user!
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CurrentUser.restoreCurrentUser();

    }
}
