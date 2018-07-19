package me.caelumterrae.fbunewsapp.client;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

import me.caelumterrae.fbunewsapp.file.PoliticalAffData;
import me.caelumterrae.fbunewsapp.math.Probability;
import me.caelumterrae.fbunewsapp.model.Post;
import me.caelumterrae.fbunewsapp.utilities.RelatedAdapter;

public class TopNewsClient extends AppCompatActivity {
    public final static String TAG = "TopNewClient";
    public final static String MEDIA_BIAS_URL = "https://raw.githubusercontent.com/drmikecrowe/mbfcext/master/docs/sources.json";
    public static final String API_KEY = "843120ac9e79440e81573a57dc13ce4f";
    public final static String API_KEY_PARAM = "apiKey";
    public final static String API_BASE_URL = "https://newsapi.org/v2"; // base API url
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

    // Convert "https://www.cnbc.com/2018/3/2..." to "cnbc.com"
    static public String trimUrl (String url) {
        final String http = "http://";
        final String https = "https://";
        final String www = "www.";

        int httpIndex = url.indexOf(http) + http.length();
        int httpsIndex = url.indexOf(https) + https.length();
        int wwwIndex = url.indexOf(www) + www.length();
        int beginIndex = Math.max(Math.max(httpIndex, httpsIndex), wwwIndex);
        String trimmedUrl = url.substring(beginIndex);
        int endIndex = trimmedUrl.indexOf("/");
        return trimmedUrl.substring(0, endIndex);
    }

    // converts bias string to political affiliation number
    // TODO: refactor to not be in this class. Not necessary for the Client. Either put in handler or in its own utilities class.
    public static int biasToNum(String bias) {
        if (bias == null) return 50;
        switch (bias) {
            case "right":
                return 100;
            case "right-center":
                return 75;
            case "center":
                return 50;
            case "leftcenter":
                return 25;
            case "left":
                return 0;
            default:
                return 50;
        }
    }
    // Retrieves ArrayList of posts of top news from newsapi.org APi
    // Pass in feedAdapter and this function will populate it with top news articles
    public void getTopNews(JsonHttpResponseHandler jsonHttpResponseHandler) {
        String url = API_BASE_URL + "/top-headlines"; // create url -- endpoint is /sources
        RequestParams params = new RequestParams();
        params.put(COUNTRY_KEY_PARAM, COUNTRY);
        params.put(NUM_RESPONSES_KEY, NUM_RESPONSES);
        params.put(API_KEY_PARAM, API_KEY); // TODO: Make Api Key Secret
        final ArrayList<Post> rawPosts = new ArrayList<>();
        client.get(url, params, jsonHttpResponseHandler);
    }

    private void populateTimeline(final ArrayList<Post> rawPosts, final FeedAdapter feedAdapter, final ArrayList<Post> posts) {
        PoliticalAffData data = new PoliticalAffData(context);
        double affiliation = data.getAffiliationNum();
        int size = rawPosts.size();
        for (int i = 0; i < size; i++) {
            int category = Probability.getCategory(affiliation);
            Post p = findPostWithCategory(rawPosts, category);
            posts.add(p);
            feedAdapter.notifyItemInserted(posts.size()-1);
        }
    }

    private Post findPostWithCategory(ArrayList<Post> rawPosts, int category) {
        for (int i = 0; i < rawPosts.size(); i++) {
            Post p = rawPosts.get(i);
            if (p.getPoliticalBias() == category) {
                rawPosts.remove(i);
                return p;
            }
        }
        // otherwise we didn't find a post with the category, so return the first one in the list
        Post p = rawPosts.get(0);
        rawPosts.remove(0);
        return p;
    }

    // Retrieves ArrayList of Posts given the related keywords from an API
    // TODO: NARROW SCOPE OF RELATED NEWS TO GET THE BEST RELATED NEWS
    public void getRelatedNews(String category, JSONArray keywords, final String originalurl, final RelatedAdapter relatedAdapter, final ArrayList<Post> posts) throws JSONException {
        String url = API_BASE_URL + "/everything";

        RequestParams categoryParams = new RequestParams();
        categoryParams.put(API_KEY_PARAM, API_KEY);
        categoryParams.put(KEYWORD_KEY_PARAM, category);
        client.get(url, categoryParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // parse the response to Post object
                // add the Post object to the arraylist
                try {
                    JSONArray results = response.getJSONArray(TopNewsClient.ROOT_NODE);
                    for (int i = 0; i < results.length(); i++) {
                        Post post = Post.fromJSON(results.getJSONObject(i));
                        if (!post.getUrl().equals(originalurl)) {
                            posts.add(post);
                            // notify adapter that a row was added
                            relatedAdapter.notifyItemInserted(posts.size() - 1); // latest item
                        }
                    }
                    Log.i("TopNewsClient", String.format("Loaded %s posts", results.length()));
                } catch (JSONException e) {
                    Log.e("TopNewsClient", "Failed to parse top posts", e);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TopNewsClient", "Failed to get data from now playing endpoint", throwable);
            }
        });

        for (int i = 0; i < keywords.length(); i++) {
            RequestParams params = new RequestParams();
            params.put(COUNTRY_KEY_PARAM, COUNTRY);
            params.put(API_KEY_PARAM, API_KEY);
            params.put(KEYWORD_KEY_PARAM, keywords.get(i));
            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // parse the response to Post object
                    // add the Post object to the arraylist
                    try {
                        JSONArray results = response.getJSONArray(TopNewsClient.ROOT_NODE);
                        for (int i = 0; i < results.length(); i++) {
                            Post post = Post.fromJSON(results.getJSONObject(i));
                            if (!post.getUrl().equals(originalurl)) {
                                Boolean postExists = false;
                                for(int j = 0; j < posts.size(); j++){
                                    if(post.getUrl().equals(posts.get(j).getUrl())){
                                        postExists = true;
                                    }
                                }
                                if (!postExists) {
                                    posts.add(post);
                                    // notify adapter that a row was added
                                    relatedAdapter.notifyItemInserted(posts.size() - 1); // latest item
                                }
                            }
                        }
                        Log.i("TopNewsClient", String.format("Loaded %s posts", results.length()));
                    } catch (JSONException e) {
                        Log.e("TopNewsClient", "Failed to parse top posts", e);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("TopNewsClient", "Failed to get data from now playing endpoint", throwable);
                }
            });
        }
    }
}
