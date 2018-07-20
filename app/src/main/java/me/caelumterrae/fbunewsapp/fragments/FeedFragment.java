package me.caelumterrae.fbunewsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.handlers.TimelineHandler;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.adapters.FeedAdapter;

public class FeedFragment extends Fragment{

    TopNewsClient client;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    FeedAdapter feedAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set client
        client = new TopNewsClient(getContext());
        //find the RecyclerView
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPost);
        //init the ArrayList (data source)
        posts = new ArrayList<>();
        //construct adapter from data source
        feedAdapter = new FeedAdapter(posts);
        //RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        //set adapter
        rvPosts.setAdapter(feedAdapter);
        // Lookup the swipe contadfgsdiner view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                feedAdapter.clear();
                posts.clear();
                client.getTopNews(new TimelineHandler(client.sourceBias, posts, feedAdapter, getContext()));
                swipeContainer.setRefreshing(false);

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.holo_blue_dark,
                android.R.color.holo_purple);

        client.getTopNews(new TimelineHandler(client.sourceBias, posts, feedAdapter, getContext()));
    }

}
