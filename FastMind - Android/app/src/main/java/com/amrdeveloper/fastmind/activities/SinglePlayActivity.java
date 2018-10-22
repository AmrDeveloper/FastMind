package com.amrdeveloper.fastmind.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amrdeveloper.fastmind.Question;
import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.utils.QuestionGenerator;

import java.util.List;

public class SinglePlayActivity extends AppCompatActivity {

    private TextView mPlayerLevel;
    private TextView mPlayerScore;
    private TextView mQuestionBody;
    private TextView mGameTimerCounter;
    private RadioGroup mGameAnswersGroup;
    private Button mGameSubmitButton;

    private boolean isGameEnd;

    private int currentGameLevel = 1;
    private String mQuestionTrueAnswer;

    private final static int GAME_TIME = 20;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_play);
        initiateViews();

        updateQuestionUI();
        onGameTimeCounter();

        mPlayerScore.setText("Score : 0");
        mPlayerLevel.setText("Level : 0");
    }

    private void initiateViews() {
        mPlayerLevel = findViewById(R.id.levelInfo);
        mPlayerScore = findViewById(R.id.scoreInfo);

        mQuestionBody = findViewById(R.id.questionBody);
        mGameTimerCounter = findViewById(R.id.gameTimer);

        mGameAnswersGroup = findViewById(R.id.answersGroup);
        mGameSubmitButton = findViewById(R.id.gameSubmit);
    }

    private void updateQuestionUI() {
        //Generate Question
        final QuestionGenerator mQuestionGenerator = new QuestionGenerator();
        Question question = mQuestionGenerator.generateQuestion(currentGameLevel);

        //Update Question
        mQuestionBody.setText(question.getQuestionBody());

        //Update Answers
        List<String> answers = question.getQuestionAnswers();

        //Update UI Radio Buttons
        for (int i = 0; i < mGameAnswersGroup.getChildCount(); i++) {
            View item = mGameAnswersGroup.getChildAt(i);
            if (item instanceof RadioButton) {
                RadioButton answersRadioButton = (RadioButton) item;
                answersRadioButton.setText(answers.get(i));
            }
        }
    }

    public void onGameTimeCounter() {
        final int[] availableTime = {GAME_TIME};
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (availableTime[0] > -1) {
                        mGameTimerCounter.setText("Timer : " + availableTime[0]-- + "s");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}
