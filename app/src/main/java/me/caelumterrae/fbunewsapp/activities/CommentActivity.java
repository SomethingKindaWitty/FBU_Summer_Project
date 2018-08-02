package me.caelumterrae.fbunewsapp.activities;

import android.media.MediaPlayer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.CommentAdapter;
import me.caelumterrae.fbunewsapp.adapters.FeedAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.database.comments.GetCommentsHandler;
import me.caelumterrae.fbunewsapp.handlers.database.comments.PostCommentHandler;
import me.caelumterrae.fbunewsapp.model.Comment;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;
import me.caelumterrae.fbunewsapp.utility.DateFunctions;

public class CommentActivity extends AppCompatActivity {

    RecyclerView rvComments;
    ArrayList<Comment> comments;
    CommentAdapter commentAdapter;
    ImageButton ibSend;
    EditText etComment;
    String url;
    ParseNewsClient parseNewsClient;
    ImageView profileImage;
    private SwipeRefreshLayout swipeContainer;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("URL");

        parseNewsClient = new ParseNewsClient(getApplicationContext());

        swipeContainer = findViewById(R.id.swipeContainer);
        rvComments = findViewById(R.id.rvComments);
        ibSend = findViewById(R.id.ibSend);
        profileImage = findViewById(R.id.tvProfileImage);
        etComment = findViewById(R.id.etComment);

        //create our quacking refresh sound
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.quack);
        final MediaPlayer quack_sound = mediaPlayer;

        // init the ArrayList (data source)
        comments = new ArrayList<>();
        // construct adapter from data source
        commentAdapter = new CommentAdapter(comments);
        // RecyclerView setup (layout manager, use adapter)
        rvComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        // set adapter
        rvComments.setAdapter(commentAdapter);

        Glide.with(getApplicationContext())
                .load(CurrentUser.getUser().getProfileUrl())
                .apply(new RequestOptions().circleCrop().placeholder(R.drawable.duckie))
                .into(profileImage);

        parseNewsClient.getComments(url, new GetCommentsHandler(getApplicationContext(), commentAdapter,
                comments));

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Manually add comment to feed and then post it to server
                // and reset EditText
                Comment c = new Comment();
                c.setComment(etComment.getText().toString());
                etComment.setText("");
                c.setDate(DateFunctions.getCurrentDate());
                c.setUid(CurrentUser.getUid());
                c.setUrl(url);
                c.setUsername(CurrentUser.getUser().getUsername());
                comments.add(0, c);
                commentAdapter.notifyItemInserted(0);

                try {
                    parseNewsClient.postComment(c.getUid(), c.getComment(), url, new
                            PostCommentHandler(getApplicationContext()));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                quack_sound.start();
                parseNewsClient.getComments(url, new GetCommentsHandler(getApplicationContext(), commentAdapter,
                        comments));
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.duck_beak,
                R.color.sea_blue, R.color.yellow_duck,
                R.color.sea_blue_light);

    }
}
