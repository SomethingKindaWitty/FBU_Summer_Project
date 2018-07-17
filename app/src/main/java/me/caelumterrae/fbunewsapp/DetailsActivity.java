package me.caelumterrae.fbunewsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.utilities.RelatedAdapter;

public class DetailsActivity extends AppCompatActivity {
    RecyclerView rvRelated;
    TextView tvTitle;
    TextView tvBody;
    ImageView ivMedia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        // populate the fields using an intent
        Post post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        tvTitle = findViewById(R.id.tvTitle);
        rvRelated = findViewById(R.id.rvRelated);
        tvBody = findViewById(R.id.tvBody);
        ivMedia = findViewById(R.id.ivMedia);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        List<Post> posts = post.getRelatedPosts();
        // Getting the related posts here.
        if (posts == null){
            posts = new ArrayList<Post>();
        }

        final RelatedAdapter relatedAdapter = new RelatedAdapter(posts);
        rvRelated.setLayoutManager(layoutManager);
        rvRelated.setAdapter(relatedAdapter);

        TopNewsClient topNewsClient = new TopNewsClient();

        final List<Post> finalPosts = posts;

        //TODO: update the trump keyword to be the keyword received from the call to our backend
        topNewsClient.getRelatedNews("trump", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // parse the response to Post object
                // add the Post object to the arraylist
                try {
                    JSONArray results = response.getJSONArray(TopNewsClient.ROOT_NODE);
                    for (int i = 0; i < results.length(); i++) {
                        Post post = Post.fromJSON(results.getJSONObject(i));
                        finalPosts.add(post);
                        // notify adapter that a row was added
                        relatedAdapter.notifyItemInserted(finalPosts.size()-1); // latest item
                    }
                    Log.i("TopNewsClient", String.format("Loaded %s posts", results.length()));
                } catch (JSONException e) {
                    Log.e("TopNewsClient", "Failed to parse top posts", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TopNewsClient", "Failed to get data from now playing endpoint", throwable);
            }
        });



        tvTitle.setText(post.getTitle());
        tvBody.setText(post.getBody());
        Glide.with(this)
                .load(post.getImageUrl())
                .apply(new RequestOptions().transform(new RoundedCornersTransformation(10,10)))
                .into(ivMedia);
    }
}
