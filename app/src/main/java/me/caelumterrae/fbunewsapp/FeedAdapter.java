package me.caelumterrae.fbunewsapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.caelumterrae.fbunewsapp.model.Post;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{

    List<Post> mPosts;
    Context context;

    //constructor for posts array
    public FeedAdapter(List<Post> posts){
        mPosts = posts;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        //grab post based on location
        Post post = mPosts.get(index);

        //populate the views
        viewHolder.tvTitle.setText(post.getTitle());
        viewHolder.tvBody.setText(post.getSummary(100));
        viewHolder.tvDate.setText(post.getDate());

        Glide.with(context)
                .load(post.getImageUrl())
                .apply(new RequestOptions().transform(new RoundedCornersTransformation(10,10)))
                .apply(RequestOptions.fitCenterTransform())
                .into(viewHolder.ivImage);
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

        public ViewHolder(View itemView){

            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.image);
            tvBody = (TextView) itemView.findViewById(R.id.body);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvDate = (TextView) itemView.findViewById(R.id.timestamp);

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
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                // show the activity
                context.startActivity(intent);
            }
        }
    }

}
