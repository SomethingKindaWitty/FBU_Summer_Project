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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

        viewHolder.username.setText(user.getUsername());
        viewHolder.body.setText(comment.getComment());
        viewHolder.title.setText(comment.getArticleTitle());
        viewHolder.timestamp.setText(comment.getRelativeTime());

        if (user.getProfileUrl().equals("") || user.getProfileUrl().equals("null")) {
            Log.e("profile image", "empty");
        } else {
            Glide.with(context)
                    .load(user.getProfileUrl())
                    .apply(new RequestOptions().circleCrop())
                    .into(viewHolder.profileImage);
        }

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
        public ImageView profileImage;
        public TextView username;
        public TextView title;
        public TextView body;
        public TextView timestamp;
        public ProgressBar pb;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            image = itemView.findViewById(R.id.image);
            profileImage = itemView.findViewById(R.id.profile);
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
