package me.caelumterrae.fbunewsapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.CommentAdapter;
import me.caelumterrae.fbunewsapp.adapters.FeedAdapter;
import me.caelumterrae.fbunewsapp.model.Comment;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class CommentActivity extends AppCompatActivity {

    RecyclerView rvComments;
    ArrayList<Comment> comments;
    CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        rvComments = findViewById(R.id.rvComments);
        // init the ArrayList (data source)
        comments = new ArrayList<>();
        // construct adapter from data source
        commentAdapter = new CommentAdapter(comments);
        // RecyclerView setup (layout manager, use adapter)
        rvComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        // set adapter
        rvComments.setAdapter(commentAdapter);

        Comment c1 = new Comment();
        c1.setComment("educated, thoughtful comment");
        c1.setDate("yesterday");
        c1.setId(1);
        c1.setUid(123);
        c1.setUrl("nothing");
        comments.add(c1);
        commentAdapter.notifyDataSetChanged();
        commentAdapter.notifyItemChanged(0);

    }
}
