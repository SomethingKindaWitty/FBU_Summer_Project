package me.caelumterrae.fbunewsapp.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.jjoe64.graphview.GraphView;

import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.adapters.UserTabsAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.CommentFragment;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.PoliticalAffiliationFragment;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.UpvotedFragment;
import me.caelumterrae.fbunewsapp.handlers.database.GetLikesHandler;
import me.caelumterrae.fbunewsapp.handlers.database.UserProfileImageHandler;
import me.caelumterrae.fbunewsapp.handlers.database.comments.GetNumCommentsHandler;
import me.caelumterrae.fbunewsapp.math.BetaDis;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.BiasHashMap;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;
import me.caelumterrae.fbunewsapp.utility.DateFunctions;

import static android.app.Activity.RESULT_OK;

public class UserFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public TextView username;
    public ImageView profileImage;
    public TextView politicalAffiliation;
    public TextView numUpvoted;
    public TextView numCommented;
    public ImageButton takePicture;
    private SwipeRefreshLayout swipeContainer;
    MediaPlayer mediaPlayer;

    private User user;
    private  ArrayList<Integer> num = new ArrayList<>(Arrays.asList(0));
    private ParseNewsClient parseNewsClient;

    // For taking/storing profile image upon Camera intent
    public final String APP_TAG = "NewsApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 219;
    public String photoFileName;
    private String imagePath = null;
    File photoFile;
    DecimalFormat df = new DecimalFormat("#.#");

    public UserFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user, container, false);
    }





    private void setupViewPager(ViewPager viewPager) {
        UserTabsAdapter adapter = new UserTabsAdapter(getChildFragmentManager());
        adapter.addFragment(new PoliticalAffiliationFragment(), "Pol. Affiliation");
        adapter.addFragment(new UpvotedFragment(), "Upvoted");
        adapter.addFragment(new CommentFragment(), "Comments");

        viewPager.setAdapter(adapter);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        swipeContainer = view.findViewById(R.id.swipeContainer);
        takePicture = view.findViewById(R.id.camera);

        user = CurrentUser.getUser();

        if (user != null){
            getNumComments();
            Log.e("url", user.getProfileUrl());
            photoFileName = user.getUsername() + DateFunctions.getTodaysTimeAndDate() + "photo.jpg";
            createUser(view, user.getUsername(), user.getPoliticalPreference(), user.getNumUpvoted(), user.getProfileUrl(), num.get(0));
            //create our quacking refresh sound
            final View view1 = view;
            //create our quacking refresh sound
            mediaPlayer = MediaPlayer.create(getContext(),R.raw.quack);
            final MediaPlayer quack_sound = mediaPlayer;

            takePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLaunchCamera();
                }
            });

            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    quack_sound.start();
                    getNumComments();
                    createUser(view1 ,user.getUsername(), user.getPoliticalPreference(), user.getNumUpvoted(), user.getProfileUrl(), num.get(0));
                    Log.e("url", user.getProfileUrl());
                    swipeContainer.setRefreshing(false);
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(R.color.duck_beak,
                    R.color.sea_blue, R.color.yellow_duck,
                    R.color.sea_blue_light);
        }

    }

    public void createUser(View view, String name, double politicalAff, int numVotes, String profileUrl, int numComments) {

        int pol_num = (int) politicalAff;

        // Sets progress circle
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, pol_num); // see this max value coming back here, we animate towards that value
        animation.setDuration(5000); // in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        username = view.findViewById(R.id.name);
        profileImage = view.findViewById(R.id.profImage);
        politicalAffiliation = view.findViewById(R.id.politicalNum);
        numUpvoted = view.findViewById(R.id.numUpvoted);
        numCommented = view.findViewById(R.id.numComments);

        username.setText(name);
        numUpvoted.setText(String.valueOf(numVotes));
        numCommented.setText(String.valueOf(numComments));

        // Rounds politicalAff to two decimal places
        df.setRoundingMode(RoundingMode.CEILING);
        double politicalRounded = Double.parseDouble(df.format(politicalAff));

        politicalAffiliation.setText(String.valueOf(politicalRounded));


        // Checks to see if the user has already set a profile image
        // and loads appropriate image
        if (!profileUrl.equals("null")) {
            Log.e("URL", profileUrl);
            Glide.with(getContext())
                    .load(profileUrl)
                    .apply(new RequestOptions().circleCrop().error(R.drawable.user))
                    .into(profileImage);
        }

    }

    // Creates the intent to the camera object of the phone and
    // creates a file path to be utilized later, storing it
    // and starting the intent
    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // Wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "me.caelumterrae.fbunewsapp", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // Ensures that the phone has an app that can handle the camera intent
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        imagePath = mediaStorageDir.getPath() + File.separator + fileName;
        return file;
    }

    // If the result from the camera intent was successful, loads new profile image
    // and sets the user
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(takenImage, 150, 150, true);
                Drawable d = new BitmapDrawable(getResources(), scaledBitmap);

                Glide.with(getContext())
                        .load(d)
                        .apply(new RequestOptions().circleCrop())
                        .into(profileImage);

                user.setProfileUrl(photoFile.getAbsolutePath());
                Log.e("url", user.getProfileUrl());
                Log.e("imagePath", imagePath);

                // Make call to database to update user
                ParseNewsClient parseNewsClient = new ParseNewsClient(getContext());
                try {
                    parseNewsClient.setProfileImage(user.getUid(), user.getProfileUrl(), new UserProfileImageHandler(getContext()));
                    Log.e("new url", user.getProfileUrl());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Sends a reference to an arraylist which will contain the number of comments a user
    // has posted to the client/handler
    public void getNumComments(){
        parseNewsClient = new ParseNewsClient(getContext());
        try {
            Log.e("num before", Integer.toString(num.get(0)));
            parseNewsClient.getNumComments(user.getUid(), new GetNumCommentsHandler(getContext(), num));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
