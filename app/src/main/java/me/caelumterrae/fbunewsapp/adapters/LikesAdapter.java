package me.caelumterrae.fbunewsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.model.Like;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {

    List<Like> likes;
    Context context;

    public LikesAdapter(List<Like> likes) {
        this.likes = likes;
    }

    @NonNull
    @Override
    public LikesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_like, viewGroup, false);
        LikesAdapter.ViewHolder viewHolder = new LikesAdapter.ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LikesAdapter.ViewHolder viewHolder, int index) {
        Like like = likes.get(index);

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

    public void clear() {
        likes.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Like> list) {
        likes.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivImage;
        public TextView tvTitle;

        public ViewHolder(View itemView){

            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

        }

        @Override
        public void onClick(View view) {
            // do nothing for now
        }

    }
}
