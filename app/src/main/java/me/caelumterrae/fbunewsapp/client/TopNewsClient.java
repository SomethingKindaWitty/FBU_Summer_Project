package me.caelumterrae.fbunewsapp.client;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.util.HashMap;

import me.caelumterrae.fbunewsapp.utility.DateFunctions;
import me.caelumterrae.fbunewsapp.utility.Timeline;

public class TopNewsClient extends AppCompatActivity {
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
    public final static Integer DAYS_BACK = 3;
    public final static String ROOT_NODE = "articles";
    public final static String NUM_RESPONSES_KEY = "pageSize";
    public final static int NUM_RESPONSES = 100;
  
    AsyncHttpClient client;
    public HashMap<String, String> sourceBias;
    Context context;


    // Instantiates new Top News Client that extracts hottest news posts from NewsApi.org
    public TopNewsClient(Context c) {
        client = new AsyncHttpClient(); // TODO: close
        sourceBias = Timeline.populateBiasHashMap(client);
        context = c;
    }

    // Retrieves ArrayList of posts of top news from newsapi.org APi
    // Pass in feedAdapter and this function will populate it with top news articles
    public void getTopNews(JsonHttpResponseHandler jsonHttpResponseHandler) {
        String url = API_BASE_URL + "/everything"; // create url -- endpoint is /sources
        String todaysDate = DateFunctions.getTodaysDate();
        RequestParams params = new RequestParams();
        params.put(LANGUAGE_PARAM, LANGUAGE);
        params.put(SOURCES_PARAM, SOURCES);
        params.put(FROM_PARAM, todaysDate);
        params.put(NUM_RESPONSES_KEY, NUM_RESPONSES);
        params.put(API_KEY_PARAM, API_KEY); // TODO: Make Api Key Secret
        client.get(url, params, jsonHttpResponseHandler);
    }


    // Retrieves ArrayList of Posts given the related keywords from an API
    public void getRelatedNews(String category, JsonHttpResponseHandler jsonHttpResponseHandler) throws JSONException {
        String url = API_BASE_URL + "/everything";
        String date = DateFunctions.getPreviousDate(DAYS_BACK);
        RequestParams params = new RequestParams();
        params.put(LANGUAGE_PARAM, LANGUAGE);
        params.put(SOURCES_PARAM, SOURCES);
        params.put(FROM_PARAM, date);
        params.put(API_KEY_PARAM, API_KEY);
        params.put(KEYWORD_KEY_PARAM, category);
        client.get(url, params, jsonHttpResponseHandler);
    }

}
