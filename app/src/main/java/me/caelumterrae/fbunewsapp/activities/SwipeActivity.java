package me.caelumterrae.fbunewsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.FragmentAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.fragments.FeedFragment;
import me.caelumterrae.fbunewsapp.fragments.GraphicsFragment;
import me.caelumterrae.fbunewsapp.fragments.UserFragment;
import me.caelumterrae.fbunewsapp.handlers.database.GetUserHandler;
import me.caelumterrae.fbunewsapp.model.User;

public class SwipeActivity extends AppCompatActivity {
    private final List<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private FragmentAdapter adapter;
    private int uid;
    private User user;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        Bundle bundle = getIntent().getExtras();
        String source = bundle.getString("source");

        // Create the placeholder fragments to be passed to the ViewPager
        fragments.add(new FeedFragment());
        fragments.add(new GraphicsFragment());
        fragments.add(new UserFragment());

        // If the source is from GetUserHandler
        // we have finished making our user call and now have the updated object
        if (source.equals("User")) {
            user = Parcels.unwrap(bundle.getParcelable("User"));
            // Creates new bundle to send info to fragments
            Bundle userobj = new Bundle();
            userobj.putParcelable("User", Parcels.wrap(user));
            // Packs bundle to fragment
            fragments.get(0).setArguments(userobj); // Feed Fragment
            fragments.get(1).setArguments(userobj); // Graphics fragment
            fragments.get(2).setArguments(userobj); // User fragment
        } else { // Every other call must trigger a call to receive the user obj
            // Pulls uid from other activities and calls ParseNewsClient
            uid = bundle.getInt("uid");
            Log.e("Uid", Integer.toString(uid));
            ParseNewsClient parseNewsClient = new ParseNewsClient(getApplicationContext());
            try {
                parseNewsClient.getUser(uid, new GetUserHandler(getApplicationContext()));
                Log.e("UserGetCall", "Got User");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Grab reference to bottom navigation view and call create function
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        createBottomNavigationView();
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

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void onBackPressed() {

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
