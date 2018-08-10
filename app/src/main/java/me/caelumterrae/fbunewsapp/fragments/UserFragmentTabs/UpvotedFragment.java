package me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.LikesAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.database.GetUserLikesHandler;
import me.caelumterrae.fbunewsapp.model.Like;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;


public class UpvotedFragment extends Fragment {

    ArrayList<Like> likes;
    RecyclerView rvLikes;
    LikesAdapter likesAdapter;
    ParseNewsClient client;

    public UpvotedFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upvoted, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = new ParseNewsClient(getContext());
        rvLikes = (RecyclerView) view.findViewById(R.id.rvLikes);
        likes = new ArrayList<>();

        likesAdapter = new LikesAdapter(likes);
        rvLikes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLikes.setAdapter(likesAdapter);

        loadRealLikes(likes, likesAdapter, rvLikes);

        rvLikes.stopScroll();
    }

    public void loadRealLikes(ArrayList<Like> likes, LikesAdapter adapter, RecyclerView rv) {
        try {
            if(client != null) {
                client.getLikesById(CurrentUser.getUid(), new GetUserLikesHandler(getContext(), likes, adapter, rv));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        loadRealLikes(likes, likesAdapter, rvLikes);
    }
}
