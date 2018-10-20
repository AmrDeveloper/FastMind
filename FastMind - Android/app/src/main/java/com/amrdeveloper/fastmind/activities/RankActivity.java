package com.amrdeveloper.fastmind.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.amrdeveloper.fastmind.R;

public class RankActivity extends AppCompatActivity {

    private ProgressBar mRankIndicator;
    private RecyclerView mRankRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        initializeViews();
    }

    /**
     * Initialize All UI Views
     */
    private void initializeViews(){
        mRankIndicator = findViewById(R.id.rankProgressBar);
        mRankRecyclerView = findViewById(R.id.rankRecyclerView);
    }
}
