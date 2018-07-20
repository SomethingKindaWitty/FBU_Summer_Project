package me.caelumterrae.fbunewsapp.fragments;

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

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.database.UserDatabase;
import me.caelumterrae.fbunewsapp.model.User;

public class UserFragment extends Fragment {

    public TextView username;
    public ImageView profileImage;
    private int userID;
    private User user;
    private UserDatabase database;
    //arbitrary object for synchronization
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

        //pull information from SwipeActivity
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
                e.printStackTrace();
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
