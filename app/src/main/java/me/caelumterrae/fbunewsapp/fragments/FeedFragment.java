package me.caelumterrae.fbunewsapp.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.FeedAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.handlers.TimelineHandler;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class FeedFragment extends Fragment{

    TopNewsClient client;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    FeedAdapter feedAdapter;
    MediaPlayer mediaPlayer;
    private SwipeRefreshLayout swipeContainer;
    private int userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userID = CurrentUser.getUid();


       Log.e("Feedfragment", Integer.toString(userID));


        // set client
        client = new TopNewsClient(getContext());
        // find the RecyclerView
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPost);
        // init the ArrayList (data source)
        posts = new ArrayList<>();
        // construct adapter from data source
        feedAdapter = new FeedAdapter(posts, userID);
        // RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        // set adapter
        rvPosts.setAdapter(feedAdapter);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        //create our quacking refresh sound
        mediaPlayer = MediaPlayer.create(getContext(),R.raw.quack);
        final MediaPlayer quack_sound = mediaPlayer;

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                quack_sound.start();
                feedAdapter.clear();
                posts.clear();
                client.getTopNews(new TimelineHandler( posts, feedAdapter, new ParseNewsClient(getContext())));
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.duck_beak,
                R.color.sea_blue, R.color.yellow_duck,
                R.color.sea_blue_light);

        client.getTopNews(new TimelineHandler(posts, feedAdapter, new ParseNewsClient(getContext())));
    }

}
