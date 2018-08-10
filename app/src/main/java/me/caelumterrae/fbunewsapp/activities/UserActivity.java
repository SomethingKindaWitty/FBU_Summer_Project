package me.caelumterrae.fbunewsapp.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.fragments.UserFragment;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class UserActivity extends AppCompatActivity {

    // It holds the user fragment inside it
    // We need this class bc we can't go directly from Activity->Fragment in CommentActivity
    // So we go Activity->Activity and hold the fragment in here

    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    FrameLayout frameLayout;

    // For swipe event
    double downX;
    double upX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        frameLayout = findViewById(R.id.frameLayout);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frameLayout, new UserFragment());
        mFragmentTransaction.commit();

    }

    // Used to override touch events for the whole activity
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return onSwipe(ev);
    }

    // Detects left or right swipe
    private boolean onSwipe(MotionEvent motionEvent) {
        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = motionEvent.getX();
                return frameLayout.onTouchEvent(motionEvent);
            case MotionEvent.ACTION_UP:
                upX = motionEvent.getX();
                double delta = upX - downX;

                if (Math.abs(delta) >= 150) {
                    if (delta >= 0 ) { // Left to right swipe
                        // Copy back button functionality
                        UserActivity.super.onBackPressed();
                    }
                }
                return frameLayout.onTouchEvent(motionEvent);
        }
        return frameLayout.onTouchEvent(motionEvent);
    }
}
