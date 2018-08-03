package me.caelumterrae.fbunewsapp.singleton;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.client.TopNewsClient;

public class BiasHashMap {

    private static HashMap<String, String> sourceBias;

    // Generates the bias hashmap -- should only be called ONCE!
    // Is called in LoginActivity onCreate.
    public static void generateSourceBias() {
        populateBiasHashMap(new AsyncHttpClient());
    }

    public static HashMap<String, String> getSourceBias() {
        return sourceBias;
    }

    /* Populates sourceBias hashmap with key=URL and value=bias.
     * Example output { `
     *  nytimes.com : leftcenter
     *  democracynow.org : left
     *  }
     */
    private static void populateBiasHashMap(AsyncHttpClient client) {
        sourceBias = new HashMap<>();
        client.get(TopNewsClient.MEDIA_BIAS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Iterator<?> keys = response.keys();
                    while (keys.hasNext()) { // iterate over JSONObject
                        String key = (String)keys.next();
                        JSONObject valueObject = response.getJSONObject(key);
                        String value = valueObject.getString("bias");
                        if (response.get(key) instanceof JSONObject ) {
                            sourceBias.put(key, value);
                            Log.i("Sourcebias", key + " : " + value);
                        }
                    }
                    Log.e("Sourcebias", "done populating hashmap");
                } catch (JSONException e) {
                    Log.e("HashMap", "Failed to parse top posts", e);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("HashMap", "Failed to get data from now playing endpoint", throwable);
            }
        });
    }
}
