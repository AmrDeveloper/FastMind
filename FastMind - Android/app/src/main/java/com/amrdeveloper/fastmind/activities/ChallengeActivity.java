package com.amrdeveloper.fastmind.activities;

import android.content.Intent;
import android.content.IntentFilter;
import androidx.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.adapter.ChallengeRecyclerAdapter;
import com.amrdeveloper.fastmind.databinding.ActivityChallengeBinding;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.receiver.NetworkReceiver;
import com.amrdeveloper.fastmind.receiver.OnConnectListener;
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

    private final Gson gson = new Gson();

    private NetworkReceiver networkReceiver;
    private ActivityChallengeBinding binding;
    private ChallengeRecyclerAdapter mChallengeRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_challenge);

        networkReceiver = new NetworkReceiver(onConnectListener);

        recyclerDefaultSettings();
        loadPlayerList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
    }

    private void loadPlayerList() {
        String requestUrl = generateUrlRequest();
        getAllPlayerForChallenge(requestUrl);
    }

    private void recyclerDefaultSettings() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mChallengeRecyclerAdapter = new ChallengeRecyclerAdapter(this);

        binding.challengeRecycler.setLayoutManager(layoutManager);
        binding.challengeRecycler.setHasFixedSize(true);
        binding.challengeRecycler.setAdapter(mChallengeRecyclerAdapter);
    }

    private String generateUrlRequest() {
        String router = "/api/players/challenge";
        String requestUrl = getString(R.string.LOCALHOST) + getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return uriBuilder.toString();
    }

    private void getAllPlayerForChallenge(String requestUrl) {
        binding.challengeProgress.setVisibility(View.VISIBLE);
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
                        binding.challengeProgress.setVisibility(View.GONE);
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

    private final OnConnectListener onConnectListener = new OnConnectListener() {
        @Override
        public void onConnected() {
            //TODO : Do No Thing For Now
        }

        @Override
        public void onDisConnected() {
            Toast.makeText(ChallengeActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChallengeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
