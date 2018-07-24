package me.caelumterrae.fbunewsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.database.UserDatabase;
import me.caelumterrae.fbunewsapp.model.User;

public class UserFragment extends Fragment {

    public TextView username;
    public ImageView profileImage;
    public TextView politicalAffiliation;
    public GraphView graph;
    private int userID;
    private User user;
    private UserDatabase database;
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
        userID = getArguments().getInt("uid");
        database = UserDatabase.getInstance(getContext());
        if (database == null) {
            Log.e("Database", "failed to create");
        } else {
            Log.e("Database", "created");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                user = database.userDao().findByID(userID);
                if (user == null) {
                    Log.e("Usernew", "not found");
                } else {
                    Log.e("Usernew", "found");
                    synchronized (object) {
                        object.notify();
                    }
                }
            }
        }).start();

        //controls thread operation order
        synchronized (object) {
            try {
                // Calling wait() will block this thread until another thread
                // calls notify() on the object.
                object.wait();
                createUser(view);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }





    }

    public void createUser(View view) {
        username = view.findViewById(R.id.name);
        profileImage = view.findViewById(R.id.profImage);
        politicalAffiliation = view.findViewById(R.id.politicalNum);
//        graph = (GraphView) view.findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);

        if (user.getUsername() == null) {
            username.setText(R.string.app_name);
        } else {
            username.setText(user.getUsername());
        }

        //TODO - get political affiliation
        politicalAffiliation.setText("duck");

        Glide.with(getContext())
                .load(R.drawable.duckie)
                .apply(new RequestOptions().fitCenter())
                .into(profileImage);


        LineChart chart = view.findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 1));
        entries.add(new Entry(3, 2));
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh

        RadarChart radarChart = view.findViewById(R.id.radarchart);
        SparseIntArray affiliation = new SparseIntArray(5);
        SparseIntArray values = new SparseIntArray(5);
        ArrayList<RadarEntry> radarEntries = new ArrayList<>();
        ArrayList<IRadarDataSet> radarDataSets = new ArrayList<>();

        affiliation.append(1, R.string.left);
        affiliation.append(2, R.string.leftmoderate);
        affiliation.append(3, R.string.moderate);
        affiliation.append(4, R.string.rightmoderate);
        affiliation.append(5, R.string.right);

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
