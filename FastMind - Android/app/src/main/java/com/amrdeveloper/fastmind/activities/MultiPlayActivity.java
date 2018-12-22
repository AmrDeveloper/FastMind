package com.amrdeveloper.fastmind.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.objects.Question;


public class MultiPlayActivity extends AppCompatActivity {

    private String player;
    private String player2;
    private int mGameLevel;

    private Question mQuestion;
    private Handler mTimerHandler;
    private Runnable mTimnerRunnable;

    //Views
    private TextView mGamePlayersInfo;
    private TextView mGameLevelInfo;
    private TextView mGameQuestion;
    private RadioGroup mGameAnswersGroup;
    private TextView mGameTimer;
    private Button mSubmitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_play);

        initiateViews();
    }

    private void initiateViews(){
        mGamePlayersInfo = findViewById(R.id.playersInfo);
        mGameLevelInfo = findViewById(R.id.levelInfo);
        mGameQuestion = findViewById(R.id.questionBody);
        mGameAnswersGroup = findViewById(R.id.answersGroup);
        mGameTimer = findViewById(R.id.gameTimer);

        mSubmitButton = findViewById(R.id.answerSubmit);
    }
}
