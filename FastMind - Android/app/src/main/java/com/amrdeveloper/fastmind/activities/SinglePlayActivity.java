package com.amrdeveloper.fastmind.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.amrdeveloper.fastmind.databinding.ActivitySinglePlayBinding;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.objects.Question;
import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.preferences.PlayerPreferences;
import com.amrdeveloper.fastmind.utils.GameDialog;
import com.amrdeveloper.fastmind.utils.QuestionGenerator;

import java.util.List;

public class SinglePlayActivity extends AppCompatActivity {

    private int mCurrentTimerValue;
    private int mQuestionTrueAnswer;
    private int mPlayerCurrentLevel;
    private int mPlayerCurrentScore;

    private Player mPlayer;
    private Handler handler;
    private Runnable runnable;
    private Question mQuestion;
    private ActivitySinglePlayBinding binding;

    private final static int GAME_TIME = 10;
    private final static String QUESTION = "question";
    private final static String TIMER = "timer";
    private final static String DEBUGGING = SinglePlayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_single_play);

        updatePlayerInformation();
        keepScreenOn();
        onGameActivityStart(savedInstanceState);
        binding.gameSubmit.setOnClickListener(v -> onGameCheckResult());
    }

    /**
     * Get Saved Player Information from Share Preferences
     */
    private void getCurrentPlayer() {
        PlayerPreferences preferences = new PlayerPreferences(this);
        mPlayer = preferences.queryPlayerInformation();
    }

    /**
     * Show Current Player Information from SharePreferences like Score and level
     */
    private void updatePlayerInformation() {
        getCurrentPlayer();
        mPlayerCurrentLevel = mPlayer.getLevel();
        mPlayerCurrentScore = mPlayer.getScore();
        binding.levelInfo.setText("Level : " + mPlayerCurrentLevel);
        binding.scoreInfo.setText("Score : " + mPlayerCurrentScore);
    }

    /**
     * Keep Current Activity Active
     */
    private void keepScreenOn() {
        binding.gameTimer.setKeepScreenOn(true);
    }

    /**
     * Set Current Question Information on UI
     */
    private void updateQuestionUI() {
        //Update Question
        binding.questionBody.setText(mQuestion.getQuestionBody());

        //Update True Answers
        mQuestionTrueAnswer = mQuestion.getTrueResult();

        //Update Answers
        List<String> answers = mQuestion.getQuestionAnswers();

        //Update UI Radio Buttons
        //for (int i = 0; i <  binding.answersGroup.getChildCount(); i++) {
        for (int i = 0; i < 4 ; i++) {
            View item =  binding.answersGroup.getChildAt(i);
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
        mQuestion = mQuestionGenerator.generateQuestion(mPlayerCurrentLevel);
    }

    /**
     * @param bundle : Bundle to check if this game is first time to launch or not
     *               : if it not first time get saved question and update UI
     *               : else create question from scratch
     */
    private void onGameActivityStart(Bundle bundle) {
        if (bundle != null) {
            mQuestion = bundle.getParcelable(QUESTION);
            mCurrentTimerValue = bundle.getInt(TIMER, 0);
            updateQuestionUI();
        } else {
            onGameCreate();
            mCurrentTimerValue = GAME_TIME;
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
        binding.answersGroup.clearCheck();
        binding.gameSubmit.setClickable(true);
        mCurrentTimerValue = GAME_TIME;
        updatePlayerInformation();
        onGameStopTimer();
        onGameTimeCounter();
        generateQuestion();
        updateQuestionUI();
    }

    /**
     * Time count down for game from 10s to 0 and update state on UI
     * when time end the game is end ans user lose
     */
    private void onGameTimeCounter() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (mCurrentTimerValue > -1) {
                        final String newTile = "Timer : " + mCurrentTimerValue-- + "s";
                        binding.gameTimer.setText(newTile);
                    } else {
                        onStop();
                        onGameStopTimer();
                        onGameLoseState();
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
        int checkedId = binding.answersGroup.getCheckedRadioButtonId();
        if (checkedId != -1) {
            binding.gameSubmit.setClickable(false);
            onGameStopTimer();
            RadioButton checkedRadioButton = findViewById(checkedId);
            String result = checkedRadioButton.getText().toString();
            if (result.equals(String.valueOf(mQuestionTrueAnswer))) {
                onGameWinStyle(checkedRadioButton);
                onGameWinState();
            } else {
                onGameLoseStyle(checkedRadioButton);
                onGameLoseState();
            }
        } else {
            Toast.makeText(this, "Select Answer First", Toast.LENGTH_SHORT).show();
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

        int checkedId = binding.answersGroup.getCheckedRadioButtonId();
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
        binding.gameSubmit.setClickable(false);
        onGameLostDialog();
    }

    /**
     * Update Player Score and level by add more score because he win
     */
    private void onGameWinAction() {
        PlayerPreferences preferences = new PlayerPreferences(this);
        preferences.playerScoreUp(mPlayerCurrentLevel);
        preferences.setPlayerLevel(mPlayerCurrentLevel);
    }

    /**
     * Update Player Score and level by sub more score because he lose
     */
    private void onGameLoseAction() {
        PlayerPreferences preferences = new PlayerPreferences(this);
        preferences.playerScoreDown(mPlayerCurrentLevel);
        preferences.setPlayerLevel(mPlayerCurrentLevel);
    }

    /**
     * This method run when game ended and  answer is true
     * it show AlertDialog with Two Buttons : Next to go to next Level
     * : Stop to back to main Activity
     */
    private void onGameWinDialog() {
        GameDialog.showSingleWinnerDialog(
                this
                , () -> {
                    mPlayerCurrentLevel++;
                    onGameCreate();
                    updatePlayerInformation();
                }
                , this::goToMainMenu);
    }

    /**
     * This method run when game ended and  answer is true
     * it show AlertDialog with One Buttons : OK to back to main Activity
     */
    private void onGameLostDialog() {
        GameDialog.showLoserDialog(this, mPlayerCurrentScore - mPlayerCurrentLevel, () -> {
            onGameLoseAction();
            goToMainMenu();
            finish();
        });
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
        onGameLoseState();
    }

    /**
     * Stop handler and runnable when before destroy activity
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        onGameStopTimer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save Current Question as Parcelable Type
        outState.putParcelable(QUESTION, mQuestion);
        outState.putInt(TIMER, mCurrentTimerValue);
    }
}
