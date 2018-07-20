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

import org.parceler.Parcels;

import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.database.UserDatabase;
import me.caelumterrae.fbunewsapp.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signUpButton;
    private List<User> userList;
    private User result;
    private UserDatabase database;
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

        database = UserDatabase.getInstance(getApplicationContext());
        if (database == null){
            Log.e("Database", "failed to create");
        }else{
            Log.e("Database", "created");
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                userList = database.userDao().getAll();
                if (userList == null){
                    Log.e("userDao", "invalid");
                }
            }
        }).start();


        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        result = database.userDao().findByName(username,password);
                        Log.e("result", "created");
                        synchronized (object) {
                            object.notify();
                        }
                    }
                }).start();

                synchronized (object) {
                    try {
                        // Calling wait() will block this thread until another thread
                        // calls notify() on the object.
                        object.wait();
                        if (result != null) {
                            int uid = result.getUid();

                            final Intent intent = new Intent(LoginActivity.this, SwipeActivity.class);
                            intent.putExtra("uid", uid);

                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Invalid login", Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException e) {
                        // Happens if someone interrupts your thread.
                    }
                }

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                try {
                    user.setUid(userList.size());
                }catch (Exception e){
                    user.setUid(0);
                }
                user.setPoliticalPreference(0);
                user.setCategories(null);
                user.setProfileUrl(null);

                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                user.setUsername(username);
                user.setPassword(password);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        result = database.userDao().findByName(username,password);
                        Log.e("result", "created");
                        synchronized (otherObject) {
                            otherObject.notify();
                        }
                    }
                }).start();

                synchronized (otherObject) {
                    try {
                        otherObject.wait();
                        if (result == null) {
                            final Intent intent = new Intent(LoginActivity.this, PoliticalActivity.class);
                            intent.putExtra("User", Parcels.wrap(user));

                            startActivity(intent);
                            finish();
                            Log.e("intent", "started");
                        } else {
                            Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_LONG).show();

                        }

                    } catch (InterruptedException e) {
                        // Happens if someone interrupts your thread.
                    }
                }
            }
        });
    }
}
