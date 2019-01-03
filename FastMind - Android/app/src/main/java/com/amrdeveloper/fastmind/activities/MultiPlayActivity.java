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

    //Views
    private TextView mGamePlayersInfo;
    private TextView mGameLevelInfo;
    private TextView mGameQuestion;
    private RadioGroup mGameAnswersGroup;
    private TextView mGameTimer;
    private Button mSubmitButton;

    private String player;
    private String player2;

    private int mGameLevel;
    private int mGameScore;
    private int mCurrentTimerValue;
    private boolean isGameEnd;

    private Question mQuestion;
    private Handler mTimerHandler;
    private Runnable mTimnerRunnable;

    private final static int GAME_TIME = 10;
    private final static String QUESTION = "question";
    private final static String TIMER = "timer";
    private static final String DEBUGGING = SinglePlayActivity.class.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_play);

        initiateViews();
        onGameActivityStart(savedInstanceState);
    }

    private void initiateViews(){
        mGamePlayersInfo = findViewById(R.id.playersInfo);
        mGameLevelInfo = findViewById(R.id.levelInfo);
        mGameQuestion = findViewById(R.id.questionBody);
        mGameAnswersGroup = findViewById(R.id.answersGroup);
        mGameTimer = findViewById(R.id.gameTimer);

        mSubmitButton = findViewById(R.id.answerSubmit);
    }

    private void onGameActivityStart(Bundle bundle) {
        if (bundle != null) {
            mQuestion = bundle.getParcelable(QUESTION);
            mCurrentTimerValue = bundle.getInt(TIMER,0);
        } else {
            //First Time
            mCurrentTimerValue = GAME_TIME;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save Current Question as Parcelable Type
        outState.putParcelable(QUESTION, mQuestion);
        outState.putInt(TIMER,mCurrentTimerValue);
    }
}
