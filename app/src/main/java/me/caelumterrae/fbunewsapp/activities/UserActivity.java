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
//    ScrollView scrollView;
    FrameLayout frameLayout;

    // For swipe event
    double downX;
    double upX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

//        scrollView = findViewById(R.id.scrollView);
        frameLayout = findViewById(R.id.frameLayout);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frameLayout, new UserFragment());
        mFragmentTransaction.commit();

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return onSwipe(motionEvent);
            }
        });
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
                Toast.makeText(getApplicationContext(), "deltaX " + Integer.toString((int) Math.round(delta)), Toast.LENGTH_SHORT).show();

                if (Math.abs(delta) >= 150) {
                    if (delta >= 0 ) {
                        //Toast.makeText(getApplicationContext(), "LEFT TO RIGHT", Toast.LENGTH_SHORT).show();
                        // Copy back button functionality
                        UserActivity.super.onBackPressed();
                    } else {
                        //Toast.makeText(getApplicationContext(), "RIGHT TO LEFT", Toast.LENGTH_SHORT).show();
                    }
                }
                return frameLayout.onTouchEvent(motionEvent);
        }
        return frameLayout.onTouchEvent(motionEvent);
    }
}
