package me.caelumterrae.fbunewsapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import org.parceler.Parcels;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.file.PoliticalAffData;

public class PoliticalActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private String affiliationNum;
    public static final String FILE_NAME = "political_affiliation.txt";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_political);
        seekBar = findViewById(R.id.sbSeekBar); // ranges from 0 (liberal) to 100 (conservative)

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("User"));

        // deprecated: loads last saved affiliation number (persistence)
    }

    // Submit Button Handler - Saves data from button and brings user to activity main
    public void onSubmit(View v) {
        // Save the political affiliation number in text file, display it on seekbar, and save it
        // to user
        affiliationNum = Integer.toString(seekBar.getProgress());

        user.setNumUpvoted(10); // gives some weight to initial self-evaluated preference
        user.setPoliticalPreference(seekBar.getProgress());

        Intent intent = new Intent(PoliticalActivity.this, CreateActivity.class);

        intent.putExtra("newUser", Parcels.wrap(user));

        seekBar.setProgress(Integer.parseInt(affiliationNum));

        startActivity(intent);
        finish();
    }

}
