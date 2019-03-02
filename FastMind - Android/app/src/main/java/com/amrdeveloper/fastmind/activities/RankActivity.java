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

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.adapter.RankRecyclerAdapter;
import com.amrdeveloper.fastmind.databinding.ActivityRankBinding;
import com.amrdeveloper.fastmind.objects.Player;
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

public class RankActivity extends AppCompatActivity {

    private final Gson gson = new Gson();

    private ActivityRankBinding binding;
    private RankRecyclerAdapter mRankRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_rank);

        recyclerDefaultSettings();
        loadingRankedPlayer();
    }

    private void recyclerDefaultSettings() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRankRecyclerAdapter = new RankRecyclerAdapter(this);
        binding.rankRecyclerView.setLayoutManager(layoutManager);
        binding.rankRecyclerView.setHasFixedSize(true);
        binding.rankRecyclerView.setAdapter(mRankRecyclerAdapter);
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

    private void loadingRankedPlayer(){
        String requestUrl = generateUrlRequest();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET,
                requestUrl,
                null,
                response -> {
                    try {
                        JSONObject resultObject = response.getJSONObject("result");
                        JSONArray playersArray = resultObject.getJSONArray("players");

                        Type listType = new TypeToken<List<Player>>() {}.getType();
                        List<Player> playerList = gson.fromJson(playersArray.toString(), listType);

                        mRankRecyclerAdapter.updateRecyclerData(playerList);
                        binding.rankProgressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    binding.rankProgressBar.setVisibility(View.GONE);
                }
        );
        queue.add(jsonRequest);
    }

    private String generateUrlRequest(){
        String router = "/api/players/rank/score";
        String requestUrl = getString(R.string.LOCALHOST) + getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return uriBuilder.toString();
    }

    private final SearchView.OnQueryTextListener mSearchTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mRankRecyclerAdapter.getFilter().filter(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String keyword) {
            mRankRecyclerAdapter.getFilter().filter(keyword);
            return false;
        }
    };
}
