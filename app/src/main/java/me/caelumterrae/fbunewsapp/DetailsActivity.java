package me.caelumterrae.fbunewsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
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
        if (posts == null){
            posts = new ArrayList<Post>();
        }
        RelatedAdapter relatedAdapter = new RelatedAdapter(posts);
        rvRelated.setLayoutManager(layoutManager);
        rvRelated.setAdapter(relatedAdapter);

        RequestOptions cropOptions = new RequestOptions().centerCrop();
        RequestOptions roundedEdges = new RequestOptions().transform(new RoundedCornersTransformation(10,10));
        RequestOptions fitCenter = new RequestOptions().fitCenter();

        tvTitle.setText(post.getTitle());
        tvBody.setText(post.getBody());
        Glide.with(this)
                .load(post.getImageUrl())
                .apply(fitCenter)
                .into(ivMedia);
    }
}
