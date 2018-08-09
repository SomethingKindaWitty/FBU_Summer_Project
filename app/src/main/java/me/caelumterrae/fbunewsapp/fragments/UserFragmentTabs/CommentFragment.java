package me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.CommentPostAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.database.comments.GetUserCommentsHandler;
import me.caelumterrae.fbunewsapp.model.Comment;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class CommentFragment extends Fragment {
    ParseNewsClient client;
    private User user;
    ArrayList<Comment> comments;
    CommentPostAdapter commentAdapter;

    public CommentFragment(){
        client = new ParseNewsClient(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("is it making it here", "is it making it here");
        user = CurrentUser.getUser();
        RecyclerView rvPost = view.findViewById(R.id.rvPost);
        comments = new ArrayList<>();
        commentAdapter = new CommentPostAdapter(comments);
        rvPost.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPost.setAdapter(commentAdapter);

        client.getCommentsById(user.getUid(), new GetUserCommentsHandler(getContext(), comments, commentAdapter));
    }

    public void refresh() {
        if (user != null) {
            client.getCommentsById(user.getUid(), new GetUserCommentsHandler(getContext(), comments, commentAdapter));
        }
    }
}
