package me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.FeedAdapter;
import me.caelumterrae.fbunewsapp.adapters.LikesAdapter;
import me.caelumterrae.fbunewsapp.adapters.UserTabsAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.TimelineHandler;
import me.caelumterrae.fbunewsapp.handlers.database.GetLikeHandler;
import me.caelumterrae.fbunewsapp.handlers.database.GetUserLikesHandler;
import me.caelumterrae.fbunewsapp.model.Like;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;


public class UpvotedFragment extends Fragment {

    ArrayList<Like> likes;
    RecyclerView rvLikes;
    LikesAdapter likesAdapter;
    ParseNewsClient client;
//    private SwipeRefreshLayout swipeContainer;

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

        // construct adapter from data source
        likesAdapter = new LikesAdapter(likes);
        // RecyclerView setup (layout manager, use adapter)
        rvLikes.setLayoutManager(new LinearLayoutManager(getContext()));
        // set adapter
        rvLikes.setAdapter(likesAdapter);

        loadRealLikes(likes, likesAdapter, rvLikes);

        rvLikes.stopScroll();
    }

    public void loadRealLikes(ArrayList<Like> likes, LikesAdapter adapter, RecyclerView rv) {
        try {
            client.getLikesById(CurrentUser.getUid(), new GetUserLikesHandler(getContext(), likes, adapter, rv));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadFakeLikes(List<Like> likes) {
        likes.clear();

        String url = "https://www.independent.co.uk/news/education/education-news/lse-students-homeless-benches-library-london-school-economics-rough-sleepers-a8481506.html";
        String image = "https://static.independent.co.uk/s3fs-public/thumbnails/image/2018/08/07/17/bench-cropped.jpg";
        String title = "Students call on London School of Economics to remove ‘anti-homeless’ benches from campus";

        for (int i = 0; i < 12; i++) {
            Like like = new Like();
            like.setImageUrl(image);
            like.setArticleUrl(url);
            like.setArticleTitle(title);
            likes.add(0,like);
        }
    }

    public void refresh() {
        loadRealLikes(likes, likesAdapter, rvLikes);
    }
}
