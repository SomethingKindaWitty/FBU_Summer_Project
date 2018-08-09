package me.caelumterrae.fbunewsapp.activities;

import android.media.MediaPlayer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.UserTabsAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.CommentFragment;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.PoliticalAffiliationFragment;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.UpvotedFragment;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class OtherUserActivity extends AppCompatActivity {

    // All of the fragments that belong ot this fragment
    public PoliticalAffiliationFragment politicalAffiliationFragment;
    public UpvotedFragment upvotedFragment;
    public CommentFragment commentFragment;
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

    }


    private void setupViewPager(ViewPager viewPager, PoliticalAffiliationFragment p, UpvotedFragment u, CommentFragment c ) {
        UserTabsAdapter adapter = new UserTabsAdapter(getSupportFragmentManager());
        adapter.addFragment(p, "Pol. Affiliation");
        adapter.addFragment(u, "Upvoted");
        adapter.addFragment(c, "Comments");
        viewPager.setAdapter(adapter);
    }
}
