package me.caelumterrae.fbunewsapp;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.io.File;

import me.caelumterrae.fbunewsapp.database.LocalUserDataSource;
import me.caelumterrae.fbunewsapp.model.User;

public class UserFragment extends Fragment{

    public TextView username;
    public ImageView profileImage;
    private LocalUserDataSource dataSource;
    private int userID;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //userID = getArguments().getInt("uid");
        //user = dataSource.getUser(userID);

        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.name);
        profileImage = view.findViewById(R.id.profImage);

        //username.setText(user.getUsername());
        username.setText("Fake Name");

        Glide.with(getContext())
                .load(R.drawable.red_footed_tortoise)
                .apply(new RequestOptions().fitCenter())
                .into(profileImage);

    }
}
