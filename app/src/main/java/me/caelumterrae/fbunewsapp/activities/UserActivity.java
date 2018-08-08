package me.caelumterrae.fbunewsapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.caelumterrae.fbunewsapp.R;

public class UserActivity extends AppCompatActivity {

    // It holds the user fragment inside it
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }
}
