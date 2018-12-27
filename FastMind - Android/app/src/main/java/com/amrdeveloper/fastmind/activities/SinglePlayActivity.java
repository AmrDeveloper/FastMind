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

    private final static int GAME_TIME = 10;
    private static final String QUESTION = "question";
    private static final String DEBUGGING = SinglePlayActivity.class.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_play);

        initiateViews();
        updatePlayerInformation();
        keepScreenOn();
        onGameActivityStart(savedInstanceState);
    }

    /**
     * Initializing All Views in This Activity
     */
    private void initiateViews() {
        mPlayerLevel = findViewById(R.id.levelInfo);
        mPlayerScore = findViewById(R.id.scoreInfo);

        mQuestionBody = findViewById(R.id.questionBody);
        mGameTimerCounter = findViewById(R.id.gameTimer);

        mGameAnswersGroup = findViewById(R.id.answersGroup);
        mGameSubmitButton = findViewById(R.id.gameSubmit);

        mGameSubmitButton.setOnClickListener(v -> onGameCheckResult());
    }

    /**
     * Show Current Player Information from SharePreferences like Score and level
     */
    private void updatePlayerInformation() {
        //TODO : Get Current Score and level then bind them
        mPlayerScore.setText("Score : 0");
        mPlayerLevel.setText("Level : 0");
    }

    /**
     * Keep Current Activity Active
     */
    private void keepScreenOn() {
        mGameTimerCounter.setKeepScreenOn(true);
    }

    /**
     * Set Current Question Information on UI
     */
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

    /**
     * Use QuestionGenerator Class to generate new Random Question for current level
     * Every Question Object contain :
     * 1 - Question Body
     * 2 - Question True Answer
     * 3 - Question Level
     * 4 - List with 4 answers one is true and other is false
     */
    private void generateQuestion() {
        final QuestionGenerator mQuestionGenerator = new QuestionGenerator();
        mQuestion = mQuestionGenerator.generateQuestion(mCurrentGameLevel);
    }

    /**
     * @param bundle : Bundle to check if this game is first time to launch or not
     *               : if it not first time get saved question and update UI
     *               : else create question from scratch
     */
    private void onGameActivityStart(Bundle bundle) {
        if (bundle != null) {
            mQuestion = bundle.getParcelable(QUESTION);
            updateQuestionUI();
        } else {
            onGameCreate();
        }
        onGameTimeCounter();
    }

    /**
     * Set default settings like default colors
     * Clear checked answers RadioButton
     * Make submit button clickable again
     * <p>
     * Generate new Question and update UI
     */
    private void onGameCreate() {
        onGameDefaultStyle();
        mGameAnswersGroup.clearCheck();
        mGameSubmitButton.setClickable(true);
        generateQuestion();
        updateQuestionUI();
    }

    /**
     * Time count down for game from 10s to 0 and update state on UI
     * when time end the game is end ans user lose
     */
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

    /**
     * Remove Runnable from Handler to stop game timer
     */
    private void onGameStopTimer() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * First make Submit Button un Clickable
     * Then get Checked RadioButton and check if this answer match question true answer
     */
    private void onGameCheckResult() {
        mGameSubmitButton.setClickable(false);
        onGameStopTimer();
        int checkedId = mGameAnswersGroup.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = findViewById(checkedId);
        String result = checkedRadioButton.getText().toString();
        if (result.equals(String.valueOf(mQuestionTrueAnswer))) {
            onGameWinStyle(checkedRadioButton);
            onGameWinState();
        } else {
            onGameLoseStyle(checkedRadioButton);
            onGameLoseState();
        }
    }

    /**
     * Return Checked RadioButtons to default style
     * TextColor : Gray
     * BackgroundColor : Black
     */
    private void onGameDefaultStyle() {
        final int black = getResources().getColor(R.color.black);
        final int gray = getResources().getColor(R.color.gray);

        int checkedId = mGameAnswersGroup.getCheckedRadioButtonId();
        if (checkedId != -1) {
            RadioButton checkedRadioButton = findViewById(checkedId);
            checkedRadioButton.setBackgroundColor(black);
            checkedRadioButton.setTextColor(gray);
        }
    }

    /**
     * @param radio : The Clicked RadioButton with true answer
     *              : Change background color to green
     *              : Change TextColor to Dark Purple to be clear
     */
    private void onGameWinStyle(RadioButton radio) {
        final int greenColor = getResources().getColor(R.color.green);
        final int darkPurpleColor = getResources().getColor(R.color.darkPurple);

        if (radio.isChecked()) {
            radio.setBackgroundColor(greenColor);
            radio.setTextColor(darkPurpleColor);
        }
    }

    /**
     * @param radio : The Clicked RadioButton with invalid answer
     *              : Change background color to Red
     *              : Change TextColor to Dark Purple to be clear
     */
    private void onGameLoseStyle(RadioButton radio) {
        final int redColor = getResources().getColor(R.color.red);
        final int darkPurpleColor = getResources().getColor(R.color.darkPurple);

        if (radio.isChecked()) {
            radio.setBackgroundColor(redColor);
            radio.setTextColor(darkPurpleColor);
        }
    }

    /**
     * Method run when player win this game
     */
    private void onGameWinState() {
        onGameWinAction();
        onGameWinDialog();
    }

    /**
     * Method run when player lose this game
     */
    private void onGameLoseState() {
        mGameSubmitButton.setClickable(false);
        onGameLostDialog();
    }

    //TODO : Action When user win a game
    private void onGameWinAction() {
        //TODO : Player Win State
        Toast.makeText(this, "GoodPlayer", Toast.LENGTH_SHORT).show();
    }

    //TODO : Action when user lose a game
    private void onGameLoseAction() {
        //TODO : Player Lose State
        //TODO : Make score = score -  point
        Toast.makeText(this, "You Lose Bro Back To Main Menu", Toast.LENGTH_SHORT).show();
        goToMainMenu();
    }

    /**
     * This method run when game ended and  answer is true
     * it show AlertDialog with Two Buttons : Next to go to next Level
     *                                      : Stop to back to main Activity
     */
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

    /**
     * This method run when game ended and  answer is true
     * it show AlertDialog with One Buttons : OK to back to main Activity
     */
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

    /**
     * back to Main Activity and destroy this activity
     */
    private void goToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Make user lose game when it back pressed while game is not ended
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO : Player State Is Loser
        onGameLoseAction();
    }

    /**
     * Stop handler and runnable when before destroy activity
     */
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
        //Save Current Question as Parcelable Type
        outState.putParcelable(QUESTION, mQuestion);
    }
}
