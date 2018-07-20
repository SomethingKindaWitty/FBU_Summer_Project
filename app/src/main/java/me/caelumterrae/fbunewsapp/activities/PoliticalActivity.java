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
    PoliticalAffData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_political);
        seekBar = findViewById(R.id.sbSeekBar); // ranges from 0 (liberal) to 100 (conservative)
       // data = new PoliticalAffData(this);

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("User"));

        // loads last saved affiliation number (persistence)
        data = new PoliticalAffData(this);
        seekBar.setProgress((int)data.getAffiliationNum());
    }

    // Submit Button Handler - Saves data from button and brings user to activity main
    public void onSubmit(View v) {
        // Save the political affiliation number in text file, display it on seekbar
        affiliationNum = Integer.toString(seekBar.getProgress());

        user.setPoliticalPreference(seekBar.getProgress());

        // store political affiliation number in political_affiliation.txt
        Intent intent = new Intent(PoliticalActivity.this, CreateActivity.class);
        intent.putExtra("newUser", Parcels.wrap(user));
      
        data.resetAffiliationNum(affiliationNum);
        seekBar.setProgress(Integer.parseInt(affiliationNum));

        startActivity(intent);
        finish();
    }

}
