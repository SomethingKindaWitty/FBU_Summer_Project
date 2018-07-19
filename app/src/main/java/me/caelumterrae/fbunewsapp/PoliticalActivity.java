package me.caelumterrae.fbunewsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;

import me.caelumterrae.fbunewsapp.database.LocalUserDataSource;
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
        //readItems(); // loads last saved affiliation number (persistence)

        // loads last saved affiliation number (persistence)
        data = new PoliticalAffData(this);
        seekBar.setProgress((int)data.getAffiliationNum());
    }

    // Submit Button Handler - Saves data from button and brings user to activity main
    public void onSubmit(View v) {
        // Save the political affiliation number in text file, display it on seekbar
        affiliationNum = Integer.toString(seekBar.getProgress());

        user.setPoliticalPreference(seekBar.getProgress());

        //writeItems(affiliationNum); // store political affiliation number in political_affiliation.txt
        Intent intent = new Intent(PoliticalActivity.this, CreateActivity.class);
        intent.putExtra("newUser", Parcels.wrap(user));
      
        data.resetAffiliationNum(affiliationNum);
        seekBar.setProgress(Integer.parseInt(affiliationNum));

        startActivity(intent);
        finish();
    }

//    // Save the political affiliation number in text file
//    private void writeItems(String text) {
//        try {
//            FileUtils.writeStringToFile(getDataFile(), text);  // writes line to FILE_NAME
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Return file which the data is stored in
//    private File getDataFile() {
//        return new File(getFilesDir(), FILE_NAME);
//    }
//
//    // Read last saved (if any) affiliation number from the file system and display it on seekbar
//    private void readItems() {
//        try {
//            affiliationNum = FileUtils.readFileToString(getDataFile());
//            seekBar.setProgress(Integer.parseInt(affiliationNum));
//        }  catch (IOException e) {
//            e.printStackTrace(); // print error to console
//        }
//    }

}
