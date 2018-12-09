package com.amrdeveloper.fastmind.activities;

import android.app.AlertDialog;
import android.app.Dialog;
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

    private int mCurrentGameLevel = 1;
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
        keepScreenOn();
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

    private void keepScreenOn() {
        mGameTimerCounter.setKeepScreenOn(true);
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

    private void generateQuestion() {
        //Generate Question
        final QuestionGenerator mQuestionGenerator = new QuestionGenerator();
        mQuestion = mQuestionGenerator.generateQuestion(mCurrentGameLevel);
    }

    private void onGameActivityStart(Bundle bundle) {
        if (bundle != null) {
            mQuestion = bundle.getParcelable(QUESTION);
            updateQuestionUI();
        } else {
            onGameCreate();
        }
        onGameTimeCounter();
    }

    private void onGameCreate() {
        mGameAnswersGroup.clearCheck();
        mGameSubmitButton.setClickable(true);
        generateQuestion();
        updateQuestionUI();
    }

    private void onGameTimeCounter() {
        final int[] availableTime = {GAME_TIME};
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (availableTime[0] > -1) {
                        String newTile = "Timer : " + availableTime[0]-- + "s";
                        mGameTimerCounter.setText(newTile);
                    } else {
                        handler.removeCallbacks(runnable);
                        onGameLoseAction();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void onGameStopTimer() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private void onGameCheckResult() {
        mGameSubmitButton.setClickable(false);
        int checkedId = mGameAnswersGroup.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = findViewById(checkedId);
        String result = checkedRadioButton.getText().toString();
        onGameStopTimer();
        if (result.equals(String.valueOf(mQuestionTrueAnswer))) {
            onGameWinStyle(checkedRadioButton);
            onGameWinState();
        } else {
            onGameLoseStyle(checkedRadioButton);
            onGameLoseState();
        }
    }

    private void onGameWinStyle(RadioButton radio) {
        final int greenColor = getResources().getColor(R.color.green);
        final int darkPurpleColor = getResources().getColor(R.color.darkPurple);

        if (radio.isChecked()) {
            radio.setBackgroundColor(greenColor);
            radio.setTextColor(darkPurpleColor);
        }
    }

    private void onGameLoseStyle(RadioButton radio) {
        final int redColor = getResources().getColor(R.color.red);
        final int darkPurpleColor = getResources().getColor(R.color.darkPurple);

        if (radio.isChecked()) {
            radio.setBackgroundColor(redColor);
            radio.setTextColor(darkPurpleColor);
        }
    }

    private void onGameWinState() {
        onGameWinAction();
        onGameWinDialog();
    }

    private void onGameLoseState() {
        mGameSubmitButton.setClickable(false);
        onGameLostDialog();
    }

    private void onGameWinAction() {
        //TODO : Player Win State
        Toast.makeText(this, "GoodPlayer", Toast.LENGTH_SHORT).show();
    }

    private void onGameLoseAction() {
        //TODO : Player Lose State
        //TODO : Make score = score -  point
        Toast.makeText(this, "You Lose Bro Back To Main Menu", Toast.LENGTH_SHORT).show();
        goToMainMenu();
    }

    private void onGameWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.StateDialogStyle);
        AlertDialog dialog = builder.create();
        dialog.setTitle(getString(R.string.state));
        dialog.setMessage(getString(R.string.win_state));
        dialog.setButton(Dialog.BUTTON_POSITIVE, getString(R.string.next), (iDialog, which) -> {
            mCurrentGameLevel++;
            onGameCreate();
            dialog.dismiss();
        });
        dialog.setButton(Dialog.BUTTON_NEGATIVE, getString(R.string.stop), (iDialog, which) -> {
            goToMainMenu();
            dialog.dismiss();
        });
        dialog.setOnDismissListener(iDialog -> {
            onGameStopTimer();
        });
        dialog.show();
    }

    private void onGameLostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.StateDialogStyle);
        AlertDialog dialog = builder.create();
        dialog.setTitle(getString(R.string.state));
        dialog.setMessage(getString(R.string.lose_state));
        dialog.setButton(Dialog.BUTTON_POSITIVE, getString(android.R.string.ok), (iDialog, which) -> {
            onGameLoseAction();
            dialog.dismiss();
        });
        dialog.setOnDismissListener(iDialog -> {
            onGameLoseAction();
        });
        dialog.show();
    }

    private void goToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO : Player State Is Loser
        onGameLoseAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(QUESTION, mQuestion);
    }
}
