package me.caelumterrae.fbunewsapp.activities;

//import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.database.UserDatabase;
import me.caelumterrae.fbunewsapp.handlers.NewsDataHandler;
import me.caelumterrae.fbunewsapp.handlers.database.LoginHandler;
import me.caelumterrae.fbunewsapp.handlers.database.SignupHandler;
import me.caelumterrae.fbunewsapp.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signUpButton;
    private List<User> userList;
    private User result;
    private UserDatabase database;
    //object values are arbitrary; used for synchronization of threads
    private final Object object = "hello";
    private final Object otherObject = "hello";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.signup);

        //grabs current instance of database
        database = UserDatabase.getInstance(getApplicationContext());
        if (database == null){
            Log.e("Database", "failed to create");
        }else{
            Log.i("Database", "created");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //userlist size used later to create uid
                //consistent adding ensures no two uids are the same
                userList = database.userDao().getAll();
                if (userList == null){
                    Log.e("userDao", "invalid");
                }
            }
        }).start();

        //checks to see if username/password combination already exists in database
        //if not, prompts invalid login
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                ParseNewsClient parseNewsClient = new ParseNewsClient(getApplicationContext());

                try {
                    parseNewsClient.login(username, password, new LoginHandler(getApplicationContext()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        result = database.userDao().findByName(username,password);
//                        Log.i("result", "created");
//                        synchronized (object) {
//                            object.notify();
//                        }
//                    }
//                }).start();
//
//                synchronized (object) {
//                    try {
//                        // block thread until respective object.notify() is called
//                        object.wait();
//                        if (result != null) {
//                            int uid = result.getUid();
//                            final Intent intent = new Intent(LoginActivity.this, SwipeActivity.class);
//                            intent.putExtra("uid", uid);
//                            startActivity(intent);
//                            finish();
//                        }else{
//                            Toast.makeText(getApplicationContext(), "Invalid login", Toast.LENGTH_LONG).show();
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }

            }
        });

        //checks to see if username/password combination already exists in database
        //if yes, denies sign up
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                User user = new User();
//                try {
//                    user.setUid(userList.size());
//                }catch (Exception e){
//                    user.setUid(0);
//                }
//                user.setPoliticalPreference(0);
//                user.setNumUpvoted(0);
//                user.setCategories(null);
//                user.setProfileUrl(null);

                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                ParseNewsClient parseNewsClient = new ParseNewsClient(getApplicationContext());

                try {
                    parseNewsClient.signup(username, password, new SignupHandler(getApplicationContext()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                user.setUsername(username);
//                user.setPassword(password);
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        result = database.userDao().findByName(username,password);
//                        Log.i("result", "created");
//                        synchronized (otherObject) {
//                            otherObject.notify();
//                        }
//                    }
//                }).start();
//
//                synchronized (otherObject) {
//                    try {
//                        // block thread until respective otherObject.notify() is called
//                        otherObject.wait();
//                        if (result == null) {
//                            final Intent intent = new Intent(LoginActivity.this, PoliticalActivity.class);
//                            intent.putExtra("User", Parcels.wrap(user));
//                            startActivity(intent);
//                            finish();
//                            Log.i("intent", "started");
//                        } else {
//                            Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_LONG).show();
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        });
    }
}
