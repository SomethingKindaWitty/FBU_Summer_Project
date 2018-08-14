package me.caelumterrae.fbunewsapp.activities;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.CommentAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.database.comments.GetCommentsHandler;
import me.caelumterrae.fbunewsapp.handlers.database.comments.PostCommentHandler;
import me.caelumterrae.fbunewsapp.model.Comment;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;
import me.caelumterrae.fbunewsapp.utility.DateFunctions;
import me.caelumterrae.fbunewsapp.utility.Keyboard;

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
    RelativeLayout relativeLayout;
    RelativeLayout innerRelativeLayout;

    // For swipe event
    double downX, downY;
    double upX, upY;

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

        relativeLayout = findViewById(R.id.relativeLayout);
        innerRelativeLayout = findViewById(R.id.innerRelativeLayout);

        // add title to toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Comments");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextAppearance(this, R.style.OpenSansLight);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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
                //quack_sound.start();
                Keyboard.hideSoftKeyboard(CommentActivity.this);
                Comment c = new Comment();
                c.setComment(etComment.getText().toString());
                etComment.setText("");
                c.setDate(DateFunctions.getCurrentDate());
                c.setUid(CurrentUser.getUid());
                c.setUrl(url);
                c.setUsername(CurrentUser.getUser().getUsername());
                c.setUserUrl(CurrentUser.getUser().getProfileUrl());
                comments.add(0, c);
                commentAdapter.notifyItemInserted(0);
                LinearLayoutManager layoutManager = (LinearLayoutManager) rvComments
                        .getLayoutManager();
                layoutManager.scrollToPositionWithOffset(0, 0);

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

    // Used to override touch events for the whole activity
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return onSwipe(ev);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    // Detects left or right swipe
    private boolean onSwipe(MotionEvent motionEvent) {
        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = motionEvent.getX();
                downY = motionEvent.getY();
                return super.dispatchTouchEvent(motionEvent);

            case MotionEvent.ACTION_UP:
                upX = motionEvent.getX();
                upY = motionEvent.getY();

                double delta = upX - downX;
                double deltaY = upY - downY;

                if (Math.abs(delta) >= 50 && Math.abs(deltaY) <= 1400) {
                    if (delta >= 0 ) {
                        // Left to right swipe
                        // Copy back button functionality
                        CommentActivity.super.onBackPressed();
                    }
                }
                return super.dispatchTouchEvent(motionEvent);
        }
        // Always go to default touch event
        return super.dispatchTouchEvent(motionEvent);
    }
}
