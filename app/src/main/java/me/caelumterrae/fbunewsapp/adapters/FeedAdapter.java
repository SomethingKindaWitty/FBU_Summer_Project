package me.caelumterrae.fbunewsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.caelumterrae.fbunewsapp.activities.DetailsActivity;
import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.model.UserLiked;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{

    List<Post> mPosts;
    Context context;
    User user;

    //constructor for posts array
    public FeedAdapter(List<Post> posts, User user)
    {
        mPosts = posts;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        //inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int index) {
        //grab post based on location
        Post post = mPosts.get(index);

        //populate the views
        viewHolder.tvTitle.setText(post.getTitle());
        viewHolder.tvBody.setText(post.getSummary(100));
        viewHolder.tvDate.setText(post.getRelativeTime());

        viewHolder.pb.setVisibility(View.VISIBLE);

        RequestOptions cropOptions = new RequestOptions().centerCrop();
        RequestOptions roundedEdges = new RequestOptions().transform(new RoundedCornersTransformation(10,10));

        if (post.getImageUrl() == null || post.getImageUrl().equals("null")){
            Log.e("ImageURL", " is null");
            viewHolder.ivImage.setVisibility(View.GONE);
            viewHolder.pb.setVisibility(View.GONE);
        } else {
            Picasso.with(context)
                    .load(post.getImageUrl())
                    .fit().centerCrop()
                    .into(viewHolder.ivImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            viewHolder.pb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            viewHolder.pb.setVisibility(View.GONE);
                        }
                    });
        }

        if (post.getSummary(100).equals("")){
            Log.e("PostBody", " is empty");
            viewHolder.tvBody.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }

    //create the ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivImage;
        public TextView tvBody;
        public TextView tvTitle;
        public TextView tvDate;
        public ProgressBar pb;

        public ViewHolder(View itemView){

            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.image);
            tvBody = (TextView) itemView.findViewById(R.id.body);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvDate = (TextView) itemView.findViewById(R.id.timestamp);
            pb = (ProgressBar) itemView.findViewById(R.id.pb);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //item position
            int position = getAdapterPosition();
            //make sure position exists in view
            if (position != RecyclerView.NO_POSITION){
                //get post
                Post post = mPosts.get(position);
                //create intent
                Intent intent = new Intent(context, DetailsActivity.class);
                // serialize the post using parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                intent.putExtra(User.class.getSimpleName(), Parcels.wrap(user));
                // show the activity
                context.startActivity(intent);
            }
        }
    }

}
