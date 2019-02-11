package com.amrdeveloper.fastmind.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.adapter.ChallengeRecyclerAdapter;
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

public class ChallengeActivity extends AppCompatActivity {

    private ProgressBar mChallengeProgress;
    private RecyclerView mChallengeRecycler;
    private ChallengeRecyclerAdapter mChallengeRecyclerAdapter;

    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        initiateViews();
        loadPlayerList();
    }

    private void initiateViews() {
        mChallengeProgress = findViewById(R.id.challengeProgress);

        mChallengeRecycler = findViewById(R.id.challengeRecycler);
        recyclerDefaultSettings();
    }

    private void loadPlayerList() {
        String requestUrl = generateUrlRequest();
        getAllPlayerForChallenge(requestUrl);
    }

    private void recyclerDefaultSettings() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mChallengeRecycler.setLayoutManager(layoutManager);
        mChallengeRecycler.setHasFixedSize(true);
        mChallengeRecyclerAdapter = new ChallengeRecyclerAdapter(this);
        mChallengeRecycler.setAdapter(mChallengeRecyclerAdapter);
    }

    private String generateUrlRequest() {
        String router = "/api/players/challenge";
        String requestUrl = getString(R.string.LOCALHOST) + getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return uriBuilder.toString();
    }

    private void getAllPlayerForChallenge(String requestUrl) {
        mChallengeProgress.setVisibility(View.VISIBLE);
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

                        mChallengeRecyclerAdapter.updateRecyclerData(playerList);
                        mChallengeProgress.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                }) {
        };
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
            mChallengeRecyclerAdapter.getFilter().filter(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String keyword) {
            mChallengeRecyclerAdapter.getFilter().filter(keyword);
            return false;
        }
    };
}
