package me.caelumterrae.fbunewsapp.handlers.database;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.utility.Format;

// This handler gets called in: UserFragment in getNumData
// This handler: will adjust the biasNums ArrayList to reflect the number of each type of article a user has liked
public class GetLikesHandler extends JsonHttpResponseHandler {
    ArrayList<Integer> biasNums;
    ArrayList<String> upvotes;
    HashMap<String, String> sourcebias;

    public GetLikesHandler(ArrayList<Integer> biasNums, HashMap<String, String> sourcebias) throws JSONException {
        Log.e("GetLikesHandler", "instantiated");
        this.upvotes = new ArrayList<>();
        this.biasNums = biasNums;
        this.sourcebias = sourcebias;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
        Log.e("GetLikesHandler", "response received");
        try {
            // Get the response and alter the list of upvoted urls accordingly
           JSONArray urls = response.getJSONArray("likes");
            for (int i=0; i < urls.length(); i++) {
                upvotes.add(urls.getString(i));
                Log.e("Liked", urls.getString(i));
            }

            // For each article, figure out it's bias number and set the ArrayList accordingly
            for (int i = 0; i < upvotes.size(); i++) {
                String biasString = sourcebias.get(Format.trimUrl(upvotes.get(i)));
                int bias = Format.biasToNum(biasString);
                Log.e("Bias",Integer.toString(bias));
                if (bias == 0) {
                    biasNums.set(0, biasNums.get(0) + 1);
                } else if (bias == 25) {
                    biasNums.set(1, biasNums.get(1) + 1);
                } else if (bias == 50) {
                    biasNums.set(2, biasNums.get(2) + 1);
                } else if (bias == 75) {
                    biasNums.set(3, biasNums.get(3) + 1);
                } else {
                    biasNums.set(4, biasNums.get(4) + 1);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetLikesHandler","failure");
    }
}
