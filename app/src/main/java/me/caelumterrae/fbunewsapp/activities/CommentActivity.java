package me.caelumterrae.fbunewsapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("URL");

        parseNewsClient = new ParseNewsClient(getApplicationContext());

        rvComments = findViewById(R.id.rvComments);
        ibSend = findViewById(R.id.ibSend);
        etComment = findViewById(R.id.etComment);
        // init the ArrayList (data source)
        comments = new ArrayList<>();
        // construct adapter from data source
        commentAdapter = new CommentAdapter(comments);
        // RecyclerView setup (layout manager, use adapter)
        rvComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        // set adapter
        rvComments.setAdapter(commentAdapter);

        parseNewsClient.getComments(url, new GetCommentsHandler(getApplicationContext(), commentAdapter,
                comments));

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Manually add comment to feed and then post it to server
                Comment c = new Comment();
                c.setComment(etComment.getText().toString());
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

//        Comment c1 = new Comment();
//        c1.setComment("educated, thoughtful comment");
//        c1.setDate("yesterday");
//        c1.setId(1);
//        c1.setUid(123);
//        c1.setUrl("nothing");
//        comments.add(0, c1);
//        commentAdapter.notifyItemChanged(0);
//
//        Comment c2 = new Comment();
//        c2.setComment("poo");
//        c2.setDate("yesterday");
//        c2.setId(2);
//        c2.setUid(123);
//        c2.setUrl("nothing");
//        comments.add(0, c2);
//        commentAdapter.notifyItemChanged(0);
//
//        for(int i = 0;  i <30; i++) {
//            comments.add(0, c2);
//            commentAdapter.notifyItemChanged(0);
//        }



    }
}
