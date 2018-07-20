package me.caelumterrae.fbunewsapp.client;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import me.caelumterrae.fbunewsapp.utility.DateFunctions;

public class TopNewsClient extends AppCompatActivity {
    public final static String TAG = "TopNewClient";
    public final static String MEDIA_BIAS_URL = "https://raw.githubusercontent.com/drmikecrowe/mbfcext/master/docs/sources.json";
    public static final String API_KEY = "843120ac9e79440e81573a57dc13ce4f";
    public final static String API_KEY_PARAM = "apiKey";
    public final static String API_BASE_URL = "https://newsapi.org/v2"; // base API url
    public final static String LANGUAGE_PARAM = "language";
    public final static String LANGUAGE = "en";
    public final static String SOURCES_PARAM = "sources";
    public final static String SOURCES = "cnn,the-new-york-times,the-huffington-post,fox-news,usa-today,reuters,politico,time,nbc-news,cnbc,cbs-news,abc-news,breitbart-news,independent,associated-press,bbc-news";
    public final static String FROM_PARAM = "from";
    public final static String COUNTRY_KEY_PARAM = "country";
    public final static String KEYWORD_KEY_PARAM = "q";
    public final static String COUNTRY = "us";
    public final static String ROOT_NODE = "articles";
    public final static String NUM_RESPONSES_KEY = "pageSize";
    public final static int NUM_RESPONSES = 100;
  
    AsyncHttpClient client;
    public HashMap<String, String> sourceBias;
    Context context;


    // Instantiates new Top News Client that extracts hottest news posts from NewsApi.org
    public TopNewsClient(Context c) {
        client = new AsyncHttpClient(); // TODO: close
        sourceBias = new HashMap<>(); //TODO: this hashmap should be moved to its own utility class as well
        populateBiasHashMap();
        context = c;
    }

    /* Populates sourceBias hashmap with key=URL and value=bias.
     * Example output { `
     *  nytimes.com : leftcenter
     *  democracynow.org : left
     *  }
     */
    private void populateBiasHashMap() {
        client.get(MEDIA_BIAS_URL, new JsonHttpResponseHandler() {
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
                            // Log.i(TAG, key + " : " + value);
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse top posts", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Failed to get data from now playing endpoint", throwable);
            }
        });
    }

    // Retrieves ArrayList of posts of top news from newsapi.org APi
    // Pass in feedAdapter and this function will populate it with top news articles
    public void getTopNews(JsonHttpResponseHandler jsonHttpResponseHandler) {
        String url = API_BASE_URL + "/everything"; // create url -- endpoint is /sources
        String todaysDate = DateFunctions.getTodaysDate();
        RequestParams params = new RequestParams();
        params.put(LANGUAGE_PARAM, LANGUAGE);
        params.put(SOURCES_PARAM, SOURCES);
        params.put(LANGUAGE_PARAM, LANGUAGE);
        params.put(FROM_PARAM, todaysDate);
        params.put(NUM_RESPONSES_KEY, NUM_RESPONSES);
        params.put(API_KEY_PARAM, API_KEY); // TODO: Make Api Key Secret
        client.get(url, params, jsonHttpResponseHandler);
    }


    // Retrieves ArrayList of Posts given the related keywords from an API
    // TODO: NARROW SCOPE OF RELATED NEWS TO GET THE BEST RELATED NEWS
    public void getRelatedNews(String category, JsonHttpResponseHandler jsonHttpResponseHandler) throws JSONException {
        String url = API_BASE_URL + "/everything";

        RequestParams categoryParams = new RequestParams();
        categoryParams.put(API_KEY_PARAM, API_KEY);
        categoryParams.put(KEYWORD_KEY_PARAM, category);
        client.get(url, categoryParams, jsonHttpResponseHandler);

//        for (int i = 0; i < keywords.length(); i++) {
//            RequestParams params = new RequestParams();
//            params.put(COUNTRY_KEY_PARAM, COUNTRY);
//            params.put(API_KEY_PARAM, API_KEY);
//            params.put(KEYWORD_KEY_PARAM, keywords.get(i));
//            client.get(url, params, new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    // parse the response to Post object
//                    // add the Post object to the arraylist
//                    try {
//                        JSONArray results = response.getJSONArray(TopNewsClient.ROOT_NODE);
//                        for (int i = 0; i < results.length(); i++) {
//                            Post post = Post.fromJSON(results.getJSONObject(i));
//                            if (!post.getUrl().equals(originalurl)) {
//                                Boolean postExists = false;
//                                for(int j = 0; j < posts.size(); j++){
//                                    if(post.getUrl().equals(posts.get(j).getUrl())){
//                                        postExists = true;
//                                    }
//                                }
//                                if (!postExists) {
//                                    posts.add(post);
//                                    // notify adapter that a row was added
//                                    relatedAdapter.notifyItemInserted(posts.size() - 1); // latest item
//                                }
//                            }
//                        }
//                        Log.i("TopNewsClient", String.format("Loaded %s posts", results.length()));
//                    } catch (JSONException e) {
//                        Log.e("TopNewsClient", "Failed to parse top posts", e);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                    Log.e("TopNewsClient", "Failed to get data from now playing endpoint", throwable);
//                }
//            });
//        }
    }
}
