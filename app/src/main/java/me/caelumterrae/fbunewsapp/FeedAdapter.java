package me.caelumterrae.fbunewsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.caelumterrae.fbunewsapp.model.Post;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{

    private List<Post> mPosts;
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
        viewHolder.tvBody.setText(post.getBody(20));

        Glide.with(context)
                .load(post.getImageUrl())
                .apply(new RequestOptions().transform(new RoundedCornersTransformation(10,10)))
                .into(viewHolder.tvImage);
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
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView tvImage;
        public TextView tvBody;
        public TextView tvTitle;

        public ViewHolder(View itemView){

            super(itemView);

            tvImage = (ImageView) itemView.findViewById(R.id.image);
            tvBody = (TextView) itemView.findViewById(R.id.body);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
        }

    }

}
