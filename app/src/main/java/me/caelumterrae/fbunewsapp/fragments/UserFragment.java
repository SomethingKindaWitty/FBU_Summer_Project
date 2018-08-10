package me.caelumterrae.fbunewsapp.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.activities.LoginActivity;
import me.caelumterrae.fbunewsapp.adapters.UserTabsAdapter;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.CommentFragment;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.PoliticalAffiliationFragment;
import me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs.UpvotedFragment;
import me.caelumterrae.fbunewsapp.handlers.database.UserProfileImageHandler;
import me.caelumterrae.fbunewsapp.handlers.database.comments.GetNumCommentsHandler;
import me.caelumterrae.fbunewsapp.model.User;
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
    public ImageButton logout;

    private SwipeRefreshLayout swipeContainer;
    MediaPlayer mediaPlayer;

    private User user;
    private  ArrayList<Integer> num = new ArrayList<>(Arrays.asList(0));
    private ParseNewsClient parseNewsClient;
    DecimalFormat df = new DecimalFormat("#.#");

    // For taking/storing profile image upon Camera intent
    public final String APP_TAG = "NewsApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 219;
    public String photoFileName;
    private String imagePath = null;
    File photoFile;
    final int MY_PERMISSIONS_REQUEST_CAMERA = 220;

    // All of the fragments that belong to this fragment
    public PoliticalAffiliationFragment politicalAffiliationFragment;
    public UpvotedFragment upvotedFragment;
    public CommentFragment commentFragment;

    public UserFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user, container, false);
    }


    private void setupViewPager(ViewPager viewPager, PoliticalAffiliationFragment p, UpvotedFragment u, CommentFragment c ) {
        UserTabsAdapter adapter = new UserTabsAdapter(getChildFragmentManager());
        adapter.addFragment(p, "Pol. Affiliation");
        adapter.addFragment(u, "Upvoted");
        adapter.addFragment(c, "Comments");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Add the fragments to the list
        politicalAffiliationFragment = new PoliticalAffiliationFragment();
        upvotedFragment = new UpvotedFragment();
        commentFragment = new CommentFragment();

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        setupViewPager(viewPager, politicalAffiliationFragment,upvotedFragment,commentFragment);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);


        swipeContainer = view.findViewById(R.id.swipeContainer);
        takePicture = view.findViewById(R.id.camera);
        logout = view.findViewById(R.id.logout);

        user = CurrentUser.getUser();

        if (user != null){
            getNumComments();
            Log.e("url", user.getProfileUrl());
            photoFileName = user.getUsername() + DateFunctions.getTodaysTimeAndDate() + "photo.jpg";
            createUser(view, user.getUsername(), user.getPoliticalPreference(), user.getNumUpvoted(), user.getProfileUrl(), num.get(0));

            // Final view to pass for refresh
            final View view1 = view;
            // Create our quacking refresh sound
            mediaPlayer = MediaPlayer.create(getContext(),R.raw.quack);
            final MediaPlayer quack_sound = mediaPlayer;

            takePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLaunchCamera();
                }
            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLogout();
                }
            });

            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    quack_sound.start();
                    getNumComments();
                    createUser(view1 ,user.getUsername(), user.getPoliticalPreference(), user.getNumUpvoted(), user.getProfileUrl(), num.get(0));
                    Log.e("url", user.getProfileUrl());

                    // Refreshes tabbed fragments
                    politicalAffiliationFragment.refresh();
                    upvotedFragment.refresh();
                    commentFragment.refresh();

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

    // Checks to see if necessary permissions exist to take picture
    public void onLaunchCamera() {
        // If permission for camera not given, make permissions request
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            // Create intent to start camera
            createCameraIntent();
        }

    }

    // Takes in the result of permission requests
    // Creates error/Toast message or allows camera intent to begin
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.e("OnRequestResult", "Accessed");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // Empty if request is canceled
                // Checks to see if permission was granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Camera", "Permission Granted");
                    createCameraIntent();
                } else {
                    Toast.makeText(getContext(), "Cannot take picture", Toast.LENGTH_LONG).show();
                    Log.e("Camera", "Permission Denied");
                }
                return;
            }
        }
    }

    // Creates the intent to the camera object of the phone and
    // creates a file path to be utilized later, storing it
    // and starting the intent
    public void createCameraIntent() {
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

    public void onLogout(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


}
