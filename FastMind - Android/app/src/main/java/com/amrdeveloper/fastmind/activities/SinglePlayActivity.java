package com.amrdeveloper.fastmind.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amrdeveloper.fastmind.R;

public class SinglePlayActivity extends AppCompatActivity {

    private TextView mPlayerLevel;
    private TextView mPlayerScore;

    private TextView mQuestionBody;
    private TextView mGameTimerCounter;

    private RadioGroup mGameAnswersGroup;

    private Button mGameSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_play);
        initiateViews();

    }

    private void initiateViews(){
        mPlayerLevel = findViewById(R.id.levelInfo);
        mPlayerScore = findViewById(R.id.scoreInfo);

        mQuestionBody = findViewById(R.id.questionBody);
        mGameTimerCounter = findViewById(R.id.gameTimer);

        mGameAnswersGroup = findViewById(R.id.answersGroup);
        mGameSubmitButton = findViewById(R.id.gameSubmit);
    }


}
