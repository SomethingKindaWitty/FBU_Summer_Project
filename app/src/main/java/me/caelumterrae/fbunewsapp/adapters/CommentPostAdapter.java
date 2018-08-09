package me.caelumterrae.fbunewsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.model.Comment;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class CommentPostAdapter extends RecyclerView.Adapter<CommentPostAdapter.ViewHolder> {
    List<Comment> mComments;
    Context context;
    User user;

    public CommentPostAdapter(List<Comment> comments) {
        mComments = comments;
        user = CurrentUser.getUser();
    }

    @NonNull
    @Override
    public CommentPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        //inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_comment_post, viewGroup, false);
        CommentPostAdapter.ViewHolder viewHolder = new CommentPostAdapter.ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentPostAdapter.ViewHolder viewHolder, int i) {
        Comment comment = mComments.get(i);
      
        viewHolder.body.setText(user.getUsername() + " said: " + comment.getComment());
        viewHolder.title.setText(comment.getArticleTitle());
        viewHolder.timestamp.setText(comment.getRelativeTime());
        if (comment.getMediaImage().equals("")) {
            Log.e("comment image", "empty");
        } else {
            Picasso.with(context)
                    .load(comment.getMediaImage())
                    .fit().centerCrop()
                    .into(viewHolder.image, new Callback() {
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

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mComments.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Comment> list) {
        mComments.addAll(list);
        notifyDataSetChanged();
    }

    //create the ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView image;
        public TextView title;
        public TextView body;
        public TextView timestamp;
        public ProgressBar pb;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            timestamp = itemView.findViewById(R.id.timestamp);
            pb = itemView.findViewById(R.id.pb);
        }

        @Override
        public void onClick(View view) {
            // For now, do nothing
        }
    }
}
