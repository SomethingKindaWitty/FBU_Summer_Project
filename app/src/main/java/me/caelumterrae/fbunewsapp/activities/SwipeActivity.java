package me.caelumterrae.fbunewsapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.FragmentAdapter;
import me.caelumterrae.fbunewsapp.fragments.FeedFragment;
import me.caelumterrae.fbunewsapp.fragments.GraphicsFragment;
import me.caelumterrae.fbunewsapp.fragments.UserFragment;

public class SwipeActivity extends AppCompatActivity {
    private final List<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private FragmentAdapter adapter;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        //pulls uid from either LoginActivity or CreateActivity
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getInt("uid");

        //creates new bundle to send info to fragments
        Bundle user = new Bundle();
        user.putInt("uid", uid);

        // Create the placeholder fragments to be passed to the ViewPager
        fragments.add(new FeedFragment());
        fragments.add(new GraphicsFragment());
        fragments.add(new UserFragment());


        //packs bundle to fragment
        fragments.get(0).setArguments(user); // Feed Fragment
        fragments.get(1).setArguments(user); // Graphics fragment
        fragments.get(2).setArguments(user); // User fragment


        // Grab a reference to view pager.
        viewPager = findViewById(R.id.viewPager);
        // Instantiate our FragmentAdapter which we will use in our ViewPager
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        // Attach our adapter to our view pager.
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }
}
