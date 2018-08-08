package me.caelumterrae.fbunewsapp.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.fragments.UserFragment;

public class UserActivity extends AppCompatActivity {

    // It holds the user fragment inside it
    // We need this class bc we can't go directly from Activity->Fragment in CommentActivity
    // So we go Activity->Activity and hold the fragment in here

    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frameLayout, new UserFragment());
        mFragmentTransaction.commit();
    }
}
