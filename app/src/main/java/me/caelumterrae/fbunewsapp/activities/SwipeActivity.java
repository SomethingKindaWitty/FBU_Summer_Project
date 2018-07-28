package me.caelumterrae.fbunewsapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

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
    public User user;
    public Semaphore semaphore;
    ParseNewsClient parseNewsClient;
    public Object lock = new Object();

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

        // Pulls uid from other activities
        uid = bundle.getInt("uid");
        Log.e("Uid", Integer.toString(uid));
        parseNewsClient = new ParseNewsClient(getApplicationContext());

        semaphore = new Semaphore(0);

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
        user = new User();
        user.setUsername("");
        final CountDownLatch latch = new CountDownLatch(1);

        try {
            parseNewsClient.getUser(uid, new GetUserHandler(user, latch));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("Making", "new thread");
        new Thread() {
            @Override
            public void run() {
             while (user.getUsername().isEmpty()) {
                 Log.e("username", "isempty");
                 try {
                     latch.await();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
            Log.e("GetUserHandler:", user.getUsername());
             Log.e("GetUserHandler:", Integer.toString(user.getUid()));
            Log.e("UserGetCall", "About 2 bundle!!!");
            // Creates new bundle to send info to fragments
            Bundle userobj = new Bundle();
            userobj.putParcelable(User.class.getSimpleName(), Parcels.wrap(user));
            // Packs bundle to fragment

            }
        }.start();
    }



    @Override
    public void onBackPressed() {

    }
}
