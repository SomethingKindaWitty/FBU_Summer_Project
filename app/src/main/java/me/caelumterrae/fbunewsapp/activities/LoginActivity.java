package me.caelumterrae.fbunewsapp.activities;

//import android.arch.persistence.room.Room;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.database.LoginHandler;
import me.caelumterrae.fbunewsapp.handlers.database.SignupHandler;
import me.caelumterrae.fbunewsapp.utility.Keyboard;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signUpButton;
    private ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.signup);
        splash = findViewById(R.id.splashScreen);
        //checks to see if username/password combination already exists in database
        //if not, prompts invalid login
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                ParseNewsClient parseNewsClient = new ParseNewsClient(getApplicationContext());

                try {
                    parseNewsClient.login(username, password, new LoginHandler(true, username, password, getApplicationContext(), LoginActivity.this,
                            splash));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //checks to see if username/password combination already exists in database
        //if yes, denies sign up
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                ParseNewsClient parseNewsClient = new ParseNewsClient(getApplicationContext());
                try {
                    parseNewsClient.login(username, password, new LoginHandler(false, username, password, getApplicationContext(), LoginActivity.this, splash));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
