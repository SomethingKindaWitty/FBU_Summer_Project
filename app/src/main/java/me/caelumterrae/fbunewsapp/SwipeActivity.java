package me.caelumterrae.fbunewsapp;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import me.caelumterrae.fbunewsapp.utilities.FragmentAdapter;

public class SwipeActivity extends AppCompatActivity {
    private final List<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        // Create the placeholder fragments to be passed to the ViewPager
        //TODO-add fragments to fragments

        // Grab a reference to view pager.
        viewPager = findViewById(R.id.viewPager);
        // Instantiate our ExampleAdapter which we will use in our ViewPager
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        // Attach our adapter to our view pager.
        viewPager.setAdapter(adapter);
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