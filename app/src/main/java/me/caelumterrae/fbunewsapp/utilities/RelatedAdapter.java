package me.caelumterrae.fbunewsapp.utilities;

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
import me.caelumterrae.fbunewsapp.DetailsActivity;
import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.model.Post;

public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.ViewHolder>{

    List<Post> relatedPosts;
    Context context;

    public RelatedAdapter(List<Post> relatedPosts) {
        this.relatedPosts = relatedPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View relatedView = inflater.inflate(R.layout.item_related_post, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(relatedView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = relatedPosts.get(position);

        RequestOptions cropOptions = new RequestOptions().centerCrop();
        RequestOptions roundedEdges = new RequestOptions().transform(new RoundedCornersTransformation(10,10));

        // Initialize the fields of all the required parts
        holder.tvTitle.setText(post.getTitle(25));
        Glide.with(context)
                .load(post.getImageUrl())
                .apply(cropOptions)
                .into(holder.ivPreview);
    }

    @Override
    public int getItemCount() {
        return relatedPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvTitle;
        public ImageView ivPreview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivPreview = itemView.findViewById(R.id.ivPreview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Post post = relatedPosts.get(position);
            Intent i = new Intent(context, DetailsActivity.class);
            i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
            context.startActivity(i);
        }
    }

}
