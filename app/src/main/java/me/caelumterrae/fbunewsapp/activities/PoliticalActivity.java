package me.caelumterrae.fbunewsapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.database.PoliticalAffHandler;
import me.caelumterrae.fbunewsapp.handlers.database.SignupHandler;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.file.PoliticalAffData;

public class PoliticalActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_political);

        Bundle bundle = getIntent().getExtras();
        uid = bundle.getInt("uid");

        seekBar = findViewById(R.id.sbSeekBar); // ranges from 0 (liberal) to 100 (conservative)

    }

    // Submit Button Handler - Saves data from button and brings user to activity main
    public void onSubmit(View v) {

        ParseNewsClient parseNewsClient = new ParseNewsClient(getApplicationContext());

        //TODO- make handler for setting political affiliation
        try {
            parseNewsClient.setAffiliation(uid, seekBar.getProgress(), new PoliticalAffHandler(getApplicationContext()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
