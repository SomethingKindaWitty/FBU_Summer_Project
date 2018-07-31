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
import com.github.mikephil.charting.components.Description;
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
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.TimelineHandler;
import me.caelumterrae.fbunewsapp.math.BetaDis;
import me.caelumterrae.fbunewsapp.handlers.database.GetUserHandler;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;

public class UserFragment extends Fragment {

    public TextView username;
    public TextView politicalAffiliationWord;
    public ImageView profileImage;
    public TextView politicalAffiliation;
    public TextView otherPoliticalAffiliation;
    public TextView numUpvoted;
    private SwipeRefreshLayout swipeContainer;
    MediaPlayer mediaPlayer;

    public GraphView graph;
    private User user;

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

        swipeContainer = view.findViewById(R.id.swipeContainer);

        user = CurrentUser.getUser();

        if (user != null){
            createUser(view, user.getUsername(), user.getPoliticalPreference(), user.getNumUpvoted());
            //create our quacking refresh sound
            final View view1 = view;
            //create our quacking refresh sound
            mediaPlayer = MediaPlayer.create(getContext(),R.raw.quack);
            final MediaPlayer quack_sound = mediaPlayer;

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

    }

    public void createUser(View view, String name, double politicalAff, int numVotes) {

        int pol_num = (int) politicalAff;

        // Sets progress circle thing
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, pol_num); // see this max value coming back here, we animate towards that value
        animation.setDuration(5000); // in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        politicalAffiliationWord = view.findViewById(R.id.politicalAffDesc);
        username = view.findViewById(R.id.name);
        profileImage = view.findViewById(R.id.profImage);
        politicalAffiliation = view.findViewById(R.id.politicalNum);
        numUpvoted = view.findViewById(R.id.numUpvoted);
        otherPoliticalAffiliation = view.findViewById(R.id.affiliationScore2);


        username.setText(name);
        numUpvoted.setText(String.valueOf(numVotes));

        // Rounds politicalAff to two decimal places
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        double politicalRounded = Double.parseDouble(df.format(politicalAff));

        politicalAffiliation.setText(String.valueOf(politicalRounded));
        otherPoliticalAffiliation.setText(String.valueOf(politicalRounded));

        // Set descriptive string
        if (politicalRounded <= 12.50) {
            politicalAffiliationWord.setText("Liberal");
        } else if (politicalRounded <= 37.50) {
            politicalAffiliationWord.setText("Moderately Liberal");
        } else if (politicalRounded <= 62.50) {
            politicalAffiliationWord.setText("Moderate");
        } else if (politicalRounded <= 87.50) {
            politicalAffiliationWord.setText("Moderately Conservative");
        } else if (politicalRounded <= 100.00) {
            politicalAffiliationWord.setText("Conservative");
        }

        Glide.with(getContext())
                .load(R.drawable.duckie)
                .apply(new RequestOptions().fitCenter())
                .into(profileImage);


        // Sets up beta distribution graph ---
        // TODO put this in another function
        BetaDis betaDis = new BetaDis(politicalRounded);
        LineChart betachart = view.findViewById(R.id.betachart);
        Description desc = new Description();
        desc.setText("Beta Distribution: Alpha: " + Integer.toString((int)betaDis.getAlpha())
        + ", Beta: " + Integer.toString((int)betaDis.getBeta()));
        betachart.setDescription(desc);
        XAxis x = betachart.getXAxis();
        x.setDrawGridLines(false);
        x.setLabelCount(10, false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis y = betachart.getAxisLeft();
        y.setEnabled(false);

        betachart.getAxisRight().setEnabled(false);

        List<Entry> beta_entries = new ArrayList<Entry>();
        for(float i = 0; i <= 1; i+=.02) {
            beta_entries.add(new Entry(i*100, (float)betaDis.getPDF(i)));
        }
        LineDataSet beta_dataSet = new LineDataSet(beta_entries, "PDF evaluated at given political affiliation");
        beta_dataSet.setDrawCircles(false);
        beta_dataSet.setLineWidth(1.8f);
        beta_dataSet.setCircleRadius(4f);
        beta_dataSet.setCircleColor(Color.BLACK);
        beta_dataSet.setHighLightColor(Color.rgb(244, 117, 117));
        beta_dataSet.setColor(Color.BLACK);
        beta_dataSet.setFillColor(Color.BLACK);
        beta_dataSet.setFillAlpha(100);
        beta_dataSet.setDrawHorizontalHighlightIndicator(false);
        beta_dataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        LineData beta_lineData = new LineData(beta_dataSet);
        beta_lineData.setDrawValues(false);
        betachart.setData(beta_lineData);
        betachart.animateXY(2000, 2000);
        betachart.invalidate();

        // set up RadarChart - TODO replace values with actual # of how many upvoted per category
        // TODO put this in another function
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
        xAxis.setTextSize(9f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mFactors = new String[]{getString(affiliation.get(1)), getString(affiliation.get(2)),
                    getString(affiliation.get(3)), getString(affiliation.get(4)), getString(affiliation.get(5))};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mFactors[(int) value % mFactors.length];
            }

        });

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setDrawLabels(false);

        radarChart.animateXY(2400, 2400, Easing.EasingOption.EaseInOutQuad, Easing.EasingOption.EaseInOutQuad);

        values.append(1, 18);
        values.append(2, 26);
        values.append(3, 35);
        values.append(4, 40);
        values.append(5, 48);

        radarEntries.clear();
        for (int i = 1; i <= 5; i++) {
            radarEntries.add(new RadarEntry(values.get(i)));
        }
        RadarDataSet dataSet2 = new RadarDataSet(radarEntries, "# of posts you upvoted across the political spectrum");
        dataSet2.setDrawFilled(true);
        radarDataSets.add(dataSet2);
        RadarData data = new RadarData(radarDataSets);
        radarChart.setData(data);
        radarChart.invalidate();

    }
}
