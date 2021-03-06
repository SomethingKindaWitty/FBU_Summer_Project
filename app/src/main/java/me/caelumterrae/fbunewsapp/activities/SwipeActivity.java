package me.caelumterrae.fbunewsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.FragmentAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.fragments.FeedFragment;
import me.caelumterrae.fbunewsapp.fragments.GraphicsFragment;
import me.caelumterrae.fbunewsapp.fragments.UserFragment;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class SwipeActivity extends AppCompatActivity {
    private final List<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private FragmentAdapter adapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_swipe);

        // Create the placeholder fragments to be passed to the ViewPager
        fragments.add(new UserFragment());
        fragments.add(new FeedFragment());
        fragments.add(new GraphicsFragment());


        // Grab reference to bottom navigation view and call create function
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        createBottomNavigationView();
        // Grab a reference to view pager.
        viewPager = findViewById(R.id.viewPager);
        // Instantiate our FragmentAdapter which we will use in our ViewPager
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        // Attach our adapter to our view pager.
        viewPager.setAdapter(adapter);
        // Sets initial page to our feed page and initial item selection
        // of bottomNavigationView to the correct icon
        viewPager.setCurrentItem(1);
        bottomNavigationView.setSelectedItemId(R.id.feed);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            // Switches item selected in bottomNavigationView based on current page
            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    bottomNavigationView.setSelectedItemId(R.id.user);
                } else if (i == 1) {
                    bottomNavigationView.setSelectedItemId(R.id.feed);
                } else {
                    bottomNavigationView.setSelectedItemId(R.id.honeycomb);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        CurrentUser.refreshUser();
    }



    @Override
    public void onBackPressed() {
        // Nothing happens if back button is pressed
    }

    // Sets the bottom navigation view's OnItemSelectedListener, which changes the page view based on
    // which item is clicked
    public void createBottomNavigationView(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id){
                    case R.id.user:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.feed:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.honeycomb:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }
}
