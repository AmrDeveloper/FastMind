package com.amrdeveloper.fastmind.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.amrdeveloper.fastmind.R;

public class ChallengeActivity extends AppCompatActivity {

    private ProgressBar mChallengeProgress;
    private RecyclerView mChallengeRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
    }

    private void initiateViews(){
        mChallengeProgress = findViewById(R.id.challengeProgress);
        mChallengeRecycler = findViewById(R.id.challengeRecycler);
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
            //TODO : Call Filter From Adapter
            //TODO : Check if no result and shot no player match keyword
            return false;
        }

        @Override
        public boolean onQueryTextChange(String keyword) {
            //TODO : Call filter from adapter
            return false;
        }
    };
}
