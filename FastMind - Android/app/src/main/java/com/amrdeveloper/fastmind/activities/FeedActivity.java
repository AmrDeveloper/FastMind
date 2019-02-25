package com.amrdeveloper.fastmind.activities;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.adapter.FeedRecyclerAdapter;
import com.amrdeveloper.fastmind.databinding.ActivityFeedBinding;
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

    private ActivityFeedBinding binding;
    private FeedRecyclerAdapter mGameFeedAdapter;

    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_feed);

        recyclerDefaultSettings();
        loadFeedFromServer();
    }

    private void recyclerDefaultSettings() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mGameFeedAdapter = new FeedRecyclerAdapter();

        binding.feedRecyclerView.setLayoutManager(layoutManager);
        binding.feedRecyclerView.setHasFixedSize(true);
        binding.feedRecyclerView.setAdapter(mGameFeedAdapter);
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
                        binding.loadingBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    binding.loadingBar.setVisibility(View.GONE);
                }
        );
        queue.add(jsonRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.search_action);

        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(mSearchTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    private final SearchView.OnQueryTextListener mSearchTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mGameFeedAdapter.getFilter().filter(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String keyword) {
            mGameFeedAdapter.getFilter().filter(keyword);
            return false;
        }
    };
}
