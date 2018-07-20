package me.caelumterrae.fbunewsapp;

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

import me.caelumterrae.fbunewsapp.database.LocalUserDataSource;
import me.caelumterrae.fbunewsapp.database.UserDatabase;
import me.caelumterrae.fbunewsapp.math.Probability;
import me.caelumterrae.fbunewsapp.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signUpButton;
    private List<User> userList;
    private User result;
    //private LocalUserDataSource dataSource;
    private UserDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.signup);

        //dataSource = new LocalUserDataSource());
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

//        try {
//            dataSource.initDb();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //userList = dataSource.getUsers();

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

//                final Intent intent = new Intent(LoginActivity.this, SwipeActivity.class);
//                startActivity(intent);
//                finish();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        result = database.userDao().findByName(username,password);
                        Log.e("result", "created");
                    }
                }).start();

                //result = dataSource.getUser(username, password);

                if (result != null) {
                    int uid = result.getUid();
                    // final Intent intent = new Intent(LoginActivity.this, SwipeActivity.class);
                    final Intent intent = new Intent(LoginActivity.this, SwipeActivity.class);
                    intent.putExtra("uid", uid);
                    //intent.putExtra("Data Source", Parcels.wrap(dataSource));
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid login", Toast.LENGTH_LONG).show();
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
                    }
                }).start();

                if (result == null) {
                    final Intent intent = new Intent(LoginActivity.this, PoliticalActivity.class);
                    intent.putExtra("User", Parcels.wrap(user));
                    //intent.putExtra("Data Source", Parcels.wrap(dataSource));
                    startActivity(intent);
                    finish();
                    Log.e("intent", "started");
                }else{
                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
