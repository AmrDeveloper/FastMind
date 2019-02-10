package com.amrdeveloper.fastmind.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.adapter.FeedRecyclerAdapter;
import com.amrdeveloper.fastmind.objects.Feed;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


public class FeedActivity extends AppCompatActivity {

    private ProgressBar mLoadingBar;
    private RecyclerView mFeedRecyclerView;
    private FeedRecyclerAdapter mGameFeedAdapter;

    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initViews();
        recyclerDefaultSettings();
        loadFeedFromServer();
    }

    private void initViews() {
        mLoadingBar = findViewById(R.id.loadingBar);
        mFeedRecyclerView = findViewById(R.id.feedRecyclerView);
    }

    private void recyclerDefaultSettings() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mFeedRecyclerView.setLayoutManager(layoutManager);
        mFeedRecyclerView.setHasFixedSize(true);
        mGameFeedAdapter = new FeedRecyclerAdapter();
        mFeedRecyclerView.setAdapter(mGameFeedAdapter);
    }

    private String generateUrlRequest() {
        String router = "/api/feeds";
        String requestUrl = getString(R.string.LOCALHOST) + getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return uriBuilder.toString();
    }

    private void loadFeedFromServer() {
        String requestUrl = generateUrlRequest();
        mLoadingBar.setVisibility(View.GONE);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET,
                requestUrl, null,
                response -> {
                    try {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray playersArray = resultObject.getJSONArray("feeds");
                        Type listType = new TypeToken<List<Feed>>() {}.getType();
                        List<Feed> feedList = gson.fromJson(playersArray.toString(), listType);

                        mGameFeedAdapter.updateFeeds(feedList);
                    } catch (JSONException e) {
                        Toast.makeText(this, "Can't Loading Data", Toast.LENGTH_SHORT).show();
                    } finally {
                        mLoadingBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    mLoadingBar.setVisibility(View.GONE);
                }
        );
        queue.add(jsonRequest);
    }
}
