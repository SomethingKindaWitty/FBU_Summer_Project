package me.caelumterrae.fbunewsapp.fragments;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.database.UserDatabase;
import me.caelumterrae.fbunewsapp.handlers.TimelineHandler;
import me.caelumterrae.fbunewsapp.math.BetaDis;
import me.caelumterrae.fbunewsapp.handlers.database.GetUserHandler;
import me.caelumterrae.fbunewsapp.model.User;

public class UserFragment extends Fragment {

    public TextView username;
    public ImageView profileImage;
    public TextView politicalAffiliation;
    public TextView otherPoliticalAffiliation;
    public TextView numUpvoted;
    private SwipeRefreshLayout swipeContainer;

    public GraphView graph;
    private User user;
    //arbitrary object for synchronization
    private final Object object = "hello";

    public UserFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            //pull information from SwipeActivity
//            JSONObject userObject = new JSONObject();
//            Semaphore semaphore = new Semaphore(0);
//            JSONObject semaphoreObj = new JSONObject();
//            semaphoreObj.put(GetUserHandler.SEMAPHORE_KEY, semaphore);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        try {
            user = Parcels.unwrap(getArguments().getParcelable("User"));
        } catch (NullPointerException e) {
            user = null;
        }

        if (user != null){
            createUser(view, user.getUsername(), user.getPoliticalPreference(), user.getNumUpvoted());
            //create our quacking refresh sound
            final MediaPlayer quack_sound = MediaPlayer.create(getContext(),R.raw.quack);
            final View view1 = view;

            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    quack_sound.start();
                    createUser(view1 ,user.getUsername(), user.getPoliticalPreference(), user.getNumUpvoted());
                    swipeContainer.setRefreshing(false);

                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(R.color.duck_beak,
                    R.color.sea_blue, R.color.yellow_duck,
                    R.color.sea_blue_light);
        }
//            Log.e("UserFrag", "Waiting for semaphore");
//            semaphore.acquire();
//            user = (User) userObject.get(GetUserHandler.USER_KEY);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


        // TODO - get user and set profile info accordingly

        //parseNewsClient.getUser(userID, new GetUserHandler(getContext()));
//        database = UserDatabase.getInstance(getContext());
//        if (database == null) {
//            Log.e("Database", "failed to create");
//        } else {
//            Log.e("Database", "created");
//        }
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                user = database.userDao().findByID(userID);
//                if (user == null) {
//                    Log.e("Usernew", "not found");
//                } else {
//                    Log.e("Usernew", "found");
//                    synchronized (object) {
//                        object.notify();
//                    }
//                }
//            }
//        }).start();

//        //controls thread operation order
//        synchronized (object) {
//            try {
//                // Calling wait() will block this thread until another thread
//                // calls notify() on the object.
//                object.wait();
//                createUser(view);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void createUser(View view, String name, double politicalAff, int numVotes) {

        // TODO - get political affiliation
        // politicalAffiliation.setText("duck");

        int pol_num = (int) politicalAff;

        // Sets progress circle thing
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, pol_num); // see this max value coming back here, we animate towards that value
        animation.setDuration(5000); // in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();


        username = view.findViewById(R.id.name);
        profileImage = view.findViewById(R.id.profImage);
        politicalAffiliation = view.findViewById(R.id.politicalNum);
        numUpvoted = view.findViewById(R.id.numUpvoted);
        otherPoliticalAffiliation = view.findViewById(R.id.affiliationScore2);


        username.setText(name);
        numUpvoted.setText(String.valueOf(numVotes));
        politicalAffiliation.setText(String.valueOf(politicalAff));
        otherPoliticalAffiliation.setText(String.valueOf(politicalAff));


       /* if (user.getUsername() == null) {
            username.setText(R.string.app_name);
        } else {
            username.setText(user.getUsername());
        }*/


        Glide.with(getContext())
                .load(R.drawable.duckie)
                .apply(new RequestOptions().fitCenter())
                .into(profileImage);


        // Sets up beta distribution graph
        BetaDis betaDis = new BetaDis(23.8);
        LineChart betachart = view.findViewById(R.id.betachart);
        betachart.setContentDescription("Beta Distribution: Alpha: 3, Beta: 9");
        XAxis x = betachart.getXAxis();
        x.setDrawGridLines(false);
        x.setLabelCount(6, false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis y = betachart.getAxisLeft();
        y.setEnabled(false);
        y.setLabelCount(6, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.BLACK);

        betachart.getAxisRight().setEnabled(false);

        List<Entry> beta_entries = new ArrayList<Entry>();
        for(float i = 0; i <= 1; i+=.02) {
            beta_entries.add(new Entry(i, (float)betaDis.getPDF(i)));
        }
        LineDataSet beta_dataSet = new LineDataSet(beta_entries, "BetaLabel");
        LineData beta_lineData = new LineData(beta_dataSet);
        betachart.setData(beta_lineData);
        betachart.animateXY(2000, 2000);
        betachart.invalidate();



        RadarChart radarChart = view.findViewById(R.id.radarchart);
        final SparseIntArray affiliation = new SparseIntArray(5);
        SparseIntArray values = new SparseIntArray(5);
        ArrayList<RadarEntry> radarEntries = new ArrayList<>();
        ArrayList<IRadarDataSet> radarDataSets = new ArrayList<>();

        affiliation.append(1, R.string.left);
        affiliation.append(2, R.string.leftmoderate);
        affiliation.append(3, R.string.moderate);
        affiliation.append(4, R.string.rightmoderate);
        affiliation.append(5, R.string.right);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setXOffset(0f);
        xAxis.setYOffset(0f);
        xAxis.setTextSize(8f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mFactors = new String[]{getString(affiliation.get(1)), getString(affiliation.get(2)),
                    getString(affiliation.get(3)), getString(affiliation.get(4)), getString(affiliation.get(5))};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mFactors[(int) value % mFactors.length];
            }

        });

        radarChart.animateXY(1400, 1400, Easing.EasingOption.EaseInOutQuad, Easing.EasingOption.EaseInOutQuad);

        values.append(1, 18);
        values.append(2, 26);
        values.append(3, 35);
        values.append(4, 40);
        values.append(5, 48);

        radarEntries.clear();
        for (int i = 1; i <= 5; i++) {
            radarEntries.add(new RadarEntry(values.get(i)));
        }
        RadarDataSet dataSet2 = new RadarDataSet(radarEntries, "");
        dataSet2.setDrawFilled(true);
        radarDataSets.add(dataSet2);
        RadarData data = new RadarData(radarDataSets);
        radarChart.setData(data);
        radarChart.invalidate();

    }
}
