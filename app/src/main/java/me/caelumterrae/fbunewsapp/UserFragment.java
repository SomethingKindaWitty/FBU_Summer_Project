package me.caelumterrae.fbunewsapp;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.File;

import me.caelumterrae.fbunewsapp.database.LocalUserDataSource;
import me.caelumterrae.fbunewsapp.database.UserDatabase;
import me.caelumterrae.fbunewsapp.model.User;

public class UserFragment extends Fragment {

    public TextView username;
    public ImageView profileImage;
    private LocalUserDataSource dataSource;
    private int userID;
    private User user;
    private UserDatabase database;
    private boolean isDone = false;
    private final Object object = "hello";

    public UserFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userID = getArguments().getInt("uid");
        database = UserDatabase.getInstance(getContext());
        if (database == null) {
            Log.e("Database", "failed to create");
        } else {
            Log.e("Database", "created");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                user = database.userDao().findByID(userID);
                if (user == null) {
                    Log.e("Usernew", "not found");
                } else {
                    Log.e("Usernew", "found");
                    synchronized (object) {
                        object.notify();
                    }
                }
            }
        }).start();

        //controls thread operation order
        synchronized (object) {
            try {
                // Calling wait() will block this thread until another thread
                // calls notify() on the object.
                object.wait();
                createUser(view);
            } catch (InterruptedException e) {
                // Happens if someone interrupts your thread.
            }
        }
    }

    public void createUser(View view) {
        username = view.findViewById(R.id.name);
        profileImage = view.findViewById(R.id.profImage);

        if (user.getUsername() == null) {
            username.setText(R.string.app_name);
        } else {
            username.setText(user.getUsername());
        }

        Glide.with(getContext())
                .load(R.drawable.red_footed_tortoise)
                .apply(new RequestOptions().fitCenter())
                .into(profileImage);
    }
}
