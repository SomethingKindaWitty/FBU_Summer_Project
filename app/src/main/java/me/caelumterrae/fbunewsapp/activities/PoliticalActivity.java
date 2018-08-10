package me.caelumterrae.fbunewsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.database.PoliticalAffHandler;
import me.caelumterrae.fbunewsapp.handlers.database.SignupHandler;
import me.caelumterrae.fbunewsapp.model.User;

import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class PoliticalActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private int uid;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_political);
        username = getIntent().getExtras().getString("username");
        password = getIntent().getExtras().getString("password");
        seekBar = findViewById(R.id.sbSeekBar); // ranges from 0 (liberal) to 100 (conservative)
    }

    // Submit Button Handler - Saves data from button and brings user to swipe activity
    public void onSubmit(View v) {
        v.setClickable(false);
        ParseNewsClient parseNewsClient = new ParseNewsClient(getApplicationContext());

        try {
            parseNewsClient.signup(username, password, seekBar.getProgress(), new SignupHandler(getApplicationContext()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
