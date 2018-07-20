package me.caelumterrae.fbunewsapp.file;


import android.content.Context;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
 * THis class helps us read and write to the political_affiliation.txt
 * We can make calls to this file to read data from the file
 * and update the file.
 */
public class PoliticalAffData {
    public static final String FILE_NAME = "political_affiliations.txt";
    Context context;
    private ArrayList<String> file_data; // line 1 = # upvotes. line 2 = current weighted average.

    public PoliticalAffData (Context c) {
        context = c;
        loadFile();
    }

    // Return file which the data is stored in
    private File getDataFile() {
        return new File(context.getFilesDir(), FILE_NAME);
    }

    public void loadFile() {
        try {
            // make array that stores the content in the file political_affiliation.txt
            file_data = new ArrayList<String>(FileUtils.readLines(getDataFile()));
        } catch (IOException e) {
            e.printStackTrace();
            file_data = new ArrayList<>();
            // We initialize file_data with our Subjective priors:
            // we say we've seen 10 articles with default 50 affiliation
            file_data.add("10");
            file_data.add("50");
        }
    }

    // Updates file_data's vote total and average
    // Called when a user upvotes or un-upvotes a file.
    public void updateFile(boolean isUpvoting, int politicalBias) {
        Log.i("Voting: before:", file_data.get(0) + ", " + file_data.get(1) + " post: " + politicalBias);
        try {
            int numVotes = Integer.parseInt(file_data.get(0));
            double voteAvg = Double.parseDouble(file_data.get(1));
            if (isUpvoting) {
                // increase num votes and calculate new average
                file_data.set(0, Integer.toString(numVotes+1));
                file_data.set(1, Double.toString((numVotes*voteAvg+politicalBias)/(numVotes+1)));
            }
            else { // decrease num votes and calculate new average
                file_data.set(0, Integer.toString(numVotes-1));
                file_data.set(1, Double.toString((numVotes*voteAvg-politicalBias)/(numVotes-1)));
            }
            Log.i("Voting: after:", file_data.get(0) + ", " + file_data.get(1));
            FileUtils.writeLines(getDataFile(), file_data); // puts file_data into FILE_NAME
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // from 0.0 [left] to 100.0 [right]
    public double getAffiliationNum() {
        return Double.parseDouble(file_data.get(1));
    }

    public void resetAffiliationNum(String affiliationNum) {
        try {
            file_data.set(0, "10");
            file_data.set(1, affiliationNum);
            FileUtils.writeLines(getDataFile(), file_data); // puts file_data into FILE_NAME
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
