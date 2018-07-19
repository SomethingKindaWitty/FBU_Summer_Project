package me.caelumterrae.fbunewsapp;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.file.PoliticalAffData;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.utilities.RelatedAdapter;

public class DetailsActivity extends AppCompatActivity {

    RecyclerView rvRelated;
    TextView tvTitle;
    TextView tvBody;
    ImageView ivMedia;
    Button upVote;
    Post post;
    Drawable main;
    Drawable drawable;
    ProgressBar pb;
    PoliticalAffData data;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        // populate the fields using an intent
        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        tvTitle = findViewById(R.id.tvTitle);
        rvRelated = findViewById(R.id.rvRelated);
        tvBody = findViewById(R.id.tvBody);
        ivMedia = findViewById(R.id.ivMedia);
        upVote = findViewById(R.id.btnLike);
        pb = findViewById(R.id.progressBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ArrayList<Post> posts = post.getRelatedPosts();
        // Getting the related posts here.
        if (posts == null) {
            posts = new ArrayList<Post>();
        }

        final RelatedAdapter relatedAdapter = new RelatedAdapter(posts);
        rvRelated.setLayoutManager(layoutManager);
        rvRelated.setAdapter(relatedAdapter);

        final TopNewsClient topNewsClient = new TopNewsClient(this);
        ParseNewsClient parseNewsClient = new ParseNewsClient(this);
        String test = post.getUrl();
        final ArrayList<Post> finalPosts = posts;
        try {
            parseNewsClient.getData(test, tvBody, relatedAdapter, finalPosts, topNewsClient, pb);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestOptions cropOptions = new RequestOptions().centerCrop();
        RequestOptions roundedEdges = new RequestOptions().transform(new RoundedCornersTransformation(10, 10));
        RequestOptions fitCenter = new RequestOptions().fitCenter();


        tvTitle.setText(post.getTitle());
        tvBody.setText(post.getBody());
        Glide.with(this)
                .load(post.getImageUrl())
                .apply(fitCenter)
                .into(ivMedia);


        // TODO put the right icon (if it was upvoted before) on page load
        main = DrawableCompat.wrap(getDrawable(android.R.drawable.ic_menu_more));
        drawable = DrawableCompat.wrap(getDrawable(android.R.drawable.ic_menu_more));
        DrawableCompat.setTint(drawable, getResources().getColor(R.color.green));

        if (post.getUpvoted()) {
            upVote.setBackground(drawable);
        } else {
            upVote.setBackground(main);
        }

        data = new PoliticalAffData(this);

    }
    //  Upvote Button Handler - Saves data from button and brings user to activity main
    public void onUpvote(View v) {
        if (post.getUpvoted()){
            post.setUpvoted(false);
            data.updateFile(false, post.getPoliticalBias());
            upVote.setBackground(main);
        } else {
            // change tint color!
            post.setUpvoted(true);
            data.updateFile(true, post.getPoliticalBias());
            upVote.setBackground(drawable);
        }
    }


//    // Return file which the data is stored in
//    private File getDataFile() {
//        return new File(getFilesDir(), UPVOTE_FILE_NAME);
//    }
//
//    // Read last saved (if any) # total upvotes and current political aff. average
//    private void loadFileData() {
//        try {
//            // make array that stores the content in the file upvote_data.txt
//            file_data = new ArrayList<String>(FileUtils.readLines(getDataFile()));
//        }  catch (IOException e) {
//            e.printStackTrace();
//            file_data = new ArrayList<>();
//            // We initialize file_data with our Subjective priors:
//            // we say we've seen 10 articles with the user's self-selected political affiliation
//            file_data.add("10");
//            file_data.add(getUsersPoliticalAff());
//        }
//    }
//
//    // reads default political affiliation number (set from PoliticalFragment)
//    private String getUsersPoliticalAff() {
//        try {
//            return FileUtils.readFileToString(new File(getFilesDir(), PoliticalActivity.FILE_NAME));
//        }  catch (IOException e) {
//            return "50"; // if file does not exist for some reason
//        }
//    }
//
//    // Updates file_data by recording vote total and averagre
//    private void updateFile(boolean isUpvoting) {
//        loadFileData(); // populates file_data with current info
//        Log.i("Voting: before:", file_data.get(0) + ", " + file_data.get(1) + " post: " + post.getPoliticalBias());
//        try {
//            int numVotes = Integer.parseInt(file_data.get(0));
//            double voteAvg = Double.parseDouble(file_data.get(1));
//            if (isUpvoting) {
//                // increase numvotes and calculate new average
//                file_data.set(0, Integer.toString(numVotes+1));
//                file_data.set(1, Double.toString((numVotes*voteAvg+post.getPoliticalBias())/(numVotes+1)));
//            }
//            else {
//                file_data.set(0, Integer.toString(numVotes-1));
//                file_data.set(1, Double.toString((numVotes*voteAvg-post.getPoliticalBias())/(numVotes-1)));
//            }
//            Log.i("Voting: after:", file_data.get(0) + ", " + file_data.get(1));
//            FileUtils.writeLines(getDataFile(), file_data); // puts file_data into upvote_data.txt
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
