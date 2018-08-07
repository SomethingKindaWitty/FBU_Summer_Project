package me.caelumterrae.fbunewsapp.adapters;

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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.model.Like;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {

    List<Like> likes;
    Context context;

    //constructor for posts array
    public LikesAdapter(List<Like> likes)
    {
        this.likes = likes;
    }

    @NonNull
    @Override
    public LikesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        //inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_like, viewGroup, false);
        LikesAdapter.ViewHolder viewHolder = new LikesAdapter.ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LikesAdapter.ViewHolder viewHolder, int index) {
        //grab post based on location
        Like like = likes.get(index);

        //populate the views
        viewHolder.tvTitle.setText(like.getArticleTitle());

        Picasso.with(context)
                .load(like.getImageUrl())
                .fit().centerCrop()
                .into(viewHolder.ivImage);
    }

    @Override
    public int getItemCount() {
        return likes.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        likes.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Like> list) {
        likes.addAll(list);
        notifyDataSetChanged();
    }

    //create the ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivImage;
        public TextView tvTitle;

        public ViewHolder(View itemView){

            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
//
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // do nothing for now
        }

        //        @Override
//        public void onClick(View view) {
//            //item position
//            int position = getAdapterPosition();
//            //make sure position exists in view
//            if (position != RecyclerView.NO_POSITION){
//                //get post
//                Post post = mPosts.get(position);
//                //create intent
//                Intent intent = new Intent(context, DetailsActivity.class);
//                // serialize the post using parceler, use its short name as a key
//                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
//                Log.e("FeedAdapter" ,"post put into intent");
//                // show the activity
//                context.startActivity(intent);
//            }
    }
}
