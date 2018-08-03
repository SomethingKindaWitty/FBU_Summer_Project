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

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.database.GetUserHandler;
import me.caelumterrae.fbunewsapp.model.Comment;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    List<Comment> mComments;
    Context context;
    User user;

    public CommentAdapter(List<Comment> comments) {
        mComments = comments;
        user = CurrentUser.getUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        //inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_comment, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Comment comment = mComments.get(i);
        User newUser = new User();

        viewHolder.tvComment.setText(comment.getComment());
        viewHolder.tvUsername.setText(comment.getUsername());
        viewHolder.tvDate.setText(comment.getRelativeTime());

//        ParseNewsClient parseNewsClient = new ParseNewsClient(context);
//
//        try {
//            parseNewsClient.getUser(comment.getUid(), new GetUserHandler(newUser, context, null));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Log.e("User url", newUser.getProfileUrl());
//        } catch (NullPointerException e) {
//            Log.e("User url", "null");
//        }

        Glide.with(context)
            .load(comment.getUserUrl())
                .apply(new RequestOptions().circleCrop().placeholder(R.drawable.duckie))
                .into(viewHolder.ivProfileImage);
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

        public ImageView ivProfileImage;
        public TextView tvComment;
        public TextView tvUsername;
        public TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        }

        @Override
        public void onClick(View view) {
            // For now, do nothing
        }
    }
}
