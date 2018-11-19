package com.amrdeveloper.fastmind.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amrdeveloper.fastmind.objects.Question;
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
    private int mQuestionTrueAnswer;

    private Handler handler;
    private Runnable runnable;
    private Question mQuestion;

    private final static int GAME_TIME = 20;
    private static final String QUESTION = "question";
    private static final String DEBUGGING = SinglePlayActivity.class.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_play);

        initiateViews();

        onGameActivityStart(savedInstanceState);

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

        mGameSubmitButton.setOnClickListener(v -> onGameCheckResult());
    }

    private void updateQuestionUI() {
        //Update Question
        mQuestionBody.setText(mQuestion.getQuestionBody());

        //Update True Answers
        mQuestionTrueAnswer = mQuestion.getTrueResult();

        //Update Answers
        List<String> answers = mQuestion.getQuestionAnswers();

        //Update UI Radio Buttons
        for (int i = 0; i < mGameAnswersGroup.getChildCount(); i++) {
            View item = mGameAnswersGroup.getChildAt(i);
            if (item instanceof RadioButton) {
                RadioButton answersRadioButton = (RadioButton) item;
                answersRadioButton.setText(answers.get(i));
            }
        }
    }

    private void generateQuestion(){
        //Generate Question
        final QuestionGenerator mQuestionGenerator = new QuestionGenerator();
        mQuestion = mQuestionGenerator.generateQuestion(currentGameLevel);
    }

    private void onGameActivityStart(Bundle bundle){
        if(bundle != null){
            mQuestion = bundle.getParcelable(QUESTION);
            updateQuestionUI();
        }else{
            generateQuestion();
            updateQuestionUI();
        }
        onGameTimeCounter();
    }

    private void onGameTimeCounter() {
        final int[] availableTime = {GAME_TIME};
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (availableTime[0] > -1) {
                        mGameTimerCounter.setText("Timer : " + availableTime[0]-- + "s");
                    }else{
                        isGameEnd = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
        if(isGameEnd){handler.removeCallbacks(runnable);}
    }

    private void onGameCheckResult(){
        int checkedId = mGameAnswersGroup.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = findViewById(checkedId);
        String result = checkedRadioButton.getText().toString();
        if(result.equals(String.valueOf(mQuestionTrueAnswer))){
            //TODO : Player Win State
            //TODO : Create Dialog To Make Player Choose if he want to go to next level or stop
            Toast.makeText(this, "GoodPlayer", Toast.LENGTH_SHORT).show();
        }else{
            //TODO : Player Lose State
            //TODO : Make score = score -  point
            //TODO : update score and back to main menu
            onGameLoseState();
        }
    }

    private void onGameLoseState() {
        Toast.makeText(this, "You Lose Bro Back To Main Menu", Toast.LENGTH_SHORT).show();
        goToMainActivity();
        finish();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler != null && runnable != null){
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(QUESTION, mQuestion);
    }
}
