package me.caelumterrae.fbunewsapp.fragments.UserFragmentTabs;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.client.ParseNewsClient;
import me.caelumterrae.fbunewsapp.handlers.database.GetLikesHandler;
import me.caelumterrae.fbunewsapp.math.BetaDis;
import me.caelumterrae.fbunewsapp.model.User;
import me.caelumterrae.fbunewsapp.singleton.BiasHashMap;
import me.caelumterrae.fbunewsapp.singleton.CurrentUser;


public class PoliticalAffiliationFragment extends Fragment {



    private User user;
    private ArrayList<Integer> biasNums;
    private HashMap <String, String> sourcebias;
    DecimalFormat df = new DecimalFormat("#.#");
    private View view;
    private double politicalRounded;

    public PoliticalAffiliationFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_political_affiliation, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        this.view = view;

        user = CurrentUser.getUser();
        // This ArrayList will later hold the respective number of articles liked per bias category
        biasNums = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0));
        sourcebias = BiasHashMap.getSourceBias();

        TextView otherPoliticalAffiliation = view.findViewById(R.id.affiliationScore2);
        TextView politicalAffiliationWord = view.findViewById(R.id.politicalAffDesc);
        df.setRoundingMode(RoundingMode.CEILING);
        this.politicalRounded = Double.parseDouble(df.format(user.getPoliticalPreference()));
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

        // Sets up beta distribution graph
        setBetaDistribution(view, politicalRounded);

        // Sets ArrayList containing number of articles in each bias category
        // for the radar chart graph
        getNumData(biasNums, sourcebias);

        // Sets up radar chart graph
        setRadarChart(view, biasNums);
    }

    public void setBetaDistribution(View view, double politicalRounded) {
        BetaDis betaDis = new BetaDis(politicalRounded);
        LineChart betachart = view.findViewById(R.id.betachart);
        Description desc = new Description();
        desc.setText("Beta Distribution: Alpha: " + df.format(betaDis.getAlpha())
                + ", Beta: " + df.format(betaDis.getBeta()));
        betachart.setDescription(desc);
        betachart.setPinchZoom(false);

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
        beta_dataSet.setCircleColor(Color.rgb(73,153,218));
        beta_dataSet.setHighLightColor(Color.rgb(239,183,66));
        beta_dataSet.setColor(Color.rgb(73,153,218));
        beta_dataSet.setFillColor(Color.rgb(73,153,218));
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
    }

    public void setRadarChart(View view, ArrayList<Integer> biasNums) {
        RadarChart radarChart = view.findViewById(R.id.radarchart);
        final SparseIntArray affiliation = new SparseIntArray(5);
        SparseIntArray values = new SparseIntArray(5);
        ArrayList<RadarEntry> radarEntries = new ArrayList<>();
        ArrayList<IRadarDataSet> radarDataSets = new ArrayList<>();
        radarChart.getDescription().setEnabled(false);
        radarChart.setWebAlpha(100);


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


        Legend l = radarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.opensans));
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setDrawLabels(false);

        radarChart.animateXY(2400, 2400, Easing.EasingOption.EaseInOutQuad, Easing.EasingOption.EaseInOutQuad);

        // Appends values to respective axes
        values.append(1, biasNums.get(0));
        values.append(2, biasNums.get(1));
        values.append(3, biasNums.get(2));
        values.append(4, biasNums.get(3));
        values.append(5, biasNums.get(4));

        radarEntries.clear();
        for (int i = 1; i <= 5; i++) {
            radarEntries.add(new RadarEntry(values.get(i)));
        }
        RadarDataSet dataSet2 = new RadarDataSet(radarEntries, "# of posts you upvoted across the political spectrum");
        dataSet2.setColor(Color.rgb(73,153,218));
        dataSet2.setFillColor(Color.rgb(73,153,218));
        dataSet2.setDrawFilled(true);
        radarDataSets.add(dataSet2);

        RadarData data = new RadarData(radarDataSets);
        data.setValueTypeface(ResourcesCompat.getFont(getActivity(), R.font.opensans));
        data.setValueTextSize(9f);
        radarChart.setData(data);
        radarChart.invalidate();
    }

    // Makes a call to the backend and passes in the ArrayList of integers to be altered
    // based on the bias of what the user has liked
    public void getNumData(ArrayList<Integer> upVotes, HashMap<String, String> sourcebias) {
        ParseNewsClient parseNewsClient = new ParseNewsClient(getContext());
        try {
            parseNewsClient.getNumLikes(user.getUid(), new GetLikesHandler(upVotes, sourcebias));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        // Sets up beta distribution graph
        setBetaDistribution(view, politicalRounded);

        // Sets ArrayList containing number of articles in each bias category
        // for the radar chart graph
        getNumData(biasNums, sourcebias);

        // Sets up radar chart graph
        setRadarChart(view, biasNums);
    }
}
