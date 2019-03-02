package com.amrdeveloper.fastmind.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.databinding.ActivityMultiPlayBinding;
import com.amrdeveloper.fastmind.objects.Question;
import com.amrdeveloper.fastmind.preferences.PlayerPreferences;
import com.amrdeveloper.fastmind.socket.Game;
import com.amrdeveloper.fastmind.socket.GameSocket;
import com.amrdeveloper.fastmind.socket.Result;
import com.amrdeveloper.fastmind.utils.GameDialog;
import com.amrdeveloper.fastmind.utils.QuestionGenerator;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MultiPlayActivity extends AppCompatActivity {

    private String player;
    private String username;

    private int mGameLevel;
    private int mGameScore;
    private int mCurrentTimerValue;
    private int mQuestionTrueAnswer;
    private boolean isOtherPlayerTrue;
    private boolean isOtherPlayerSubmit;

    private Question mQuestion;
    private Handler mTimerHandler;
    private Runnable mTimerRunnable;
    private Socket mGameSocket;
    private final Gson gson = new Gson();

    private Dialog mGameWaitDialog;
    private ActivityMultiPlayBinding binding;
    private final Context CONTEXT = MultiPlayActivity.this;

    private static final int GAME_TIME = 10;
    private static final String QUESTION = "question";
    private static final String TIMER = "timer";
    private static final String DEBUGGING = SinglePlayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_multi_play);

        keepScreenOn();
        onGameActivityStart(savedInstanceState);
        connectToServer();
        getGameInformation();
        updateGameUI();
        onGameTimeCounter();
        binding.answerSubmit.setOnClickListener(view -> onGameSubmitButton());
    }

    private void getGameInformation() {
        Intent intent = getIntent();
        player = intent.getStringExtra("sender");
        username = intent.getStringExtra("receiver");
        mQuestion = intent.getParcelableExtra("question");
        mGameLevel = mQuestion.getQuestionLevel();
        mGameScore = mGameLevel;
    }

    private void updateGameUI() {
        String gameInformation = username + " VS " + player;
        String gameLevel = "Level : " + mGameLevel;
        binding.playersInfo.setText(gameInformation);
        binding.levelInfo.setText(gameLevel);

        //Update Question
        binding.questionBody.setText(mQuestion.getQuestionBody());

        //Update True Answers
        mQuestionTrueAnswer = mQuestion.getTrueResult();

        //Update Answers
        List<String> answers = mQuestion.getQuestionAnswers();

        //Update UI Radio Buttons
        for (int i = 0; i < binding.answersGroup.getChildCount(); i++) {
            View item = binding.answersGroup.getChildAt(i);
            if (item instanceof RadioButton) {
                RadioButton answersRadioButton = (RadioButton) item;
                answersRadioButton.setText(answers.get(i));
            }
        }
    }

    private void keepScreenOn() {
        binding.gameTimer.setKeepScreenOn(true);
    }

    private void connectToServer() {
        mGameSocket = GameSocket.getSocket(this);
        mGameSocket.on(Game.PLAY, onPlayListener);
    }

    private void onGameActivityStart(Bundle bundle) {
        if (bundle != null) {
            mQuestion = bundle.getParcelable(QUESTION);
            mCurrentTimerValue = bundle.getInt(TIMER, 0);
        } else {
            //First Time
            mCurrentTimerValue = GAME_TIME;
        }
    }

    private void onGameTimeCounter() {
        mTimerHandler = new Handler();
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (mCurrentTimerValue > -1) {
                        final String newTile = "Timer : " + mCurrentTimerValue-- + "s";
                        binding.gameTimer.setText(newTile);
                    } else {
                        onGameStopTimer();
                        if(mGameWaitDialog != null)mGameWaitDialog.dismiss();
                        //TODO : Implement User Lose State
                        //TODO : Score = score -  current level
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mTimerHandler.postDelayed(this, 1000);
            }
        };
        mTimerHandler.postDelayed(mTimerRunnable, 1000);
    }

    private void onGameStopTimer() {
        if (mTimerHandler != null && mTimerRunnable != null) {
            mTimerHandler.removeCallbacks(mTimerRunnable);
        }
    }

    private void onGameSubmitButton() {
        int checkedId = binding.answersGroup.getCheckedRadioButtonId();
        if (checkedId != -1) {
            binding.answerSubmit.setClickable(false);
            onGameStopTimer();
            RadioButton checkedRadioButton = findViewById(checkedId);
            String result = checkedRadioButton.getText().toString();
            if (result.equals(String.valueOf(mQuestionTrueAnswer))) {
                if (isOtherPlayerSubmit) {
                    if(mGameWaitDialog != null)mGameWaitDialog.dismiss();
                    if (isOtherPlayerTrue) {
                        mGameLevel++;
                        //Return Settings to Default
                        isOtherPlayerTrue = false;
                        isOtherPlayerSubmit = false;
                        binding.answerSubmit.setClickable(true);
                        binding.answersGroup.clearCheck();
                        QuestionGenerator generator = new QuestionGenerator();
                        mQuestion = generator.generateQuestion(mGameLevel);
                        mGameSocket.emit(Game.PLAY, Game.NEXT, username, player, gson.toJson(mQuestion));
                        updateGameUI();
                        mCurrentTimerValue = 10;
                        onGameStopTimer();
                        onGameTimeCounter();
                    } else {
                        mGameSocket.emit(Game.PLAY, Game.END, username, player, Result.SENDER_PLAYER_WINNER);
                        GameDialog.showWinnerDialog(this, mGameLevel, this::backToMainMenu);
                    }
                } else {
                    mGameSocket.emit(Game.PLAY, Game.ANSWER, username, player, Result.VALID_RESULT);
                    mGameWaitDialog = GameDialog.showWaitDialog(this);
                }
            } else {
                if (isOtherPlayerSubmit) {
                    if(mGameWaitDialog != null)mGameWaitDialog.dismiss();
                    if (isOtherPlayerTrue) {
                        mGameSocket.emit(Game.PLAY, Game.END, username, player, Result.RECEIVER_PLAYER_WINNER);
                        GameDialog.showLoserDialog(this, mGameLevel, this::backToMainMenu);
                    } else {
                        mGameSocket.emit(Game.PLAY, Game.END, username, player, Result.TWO_PLAYERS_LOSER);
                        GameDialog.showLoserDialog(this, mGameLevel, this::backToMainMenu);
                    }
                } else {
                    mGameSocket.emit(Game.PLAY, Game.ANSWER, username, player, Result.INVALID_RESULT);
                    mGameWaitDialog = GameDialog.showWaitDialog(this);
                }
            }
        } else {
            Toast.makeText(this, "Select Answer First", Toast.LENGTH_SHORT).show();
        }
    }

    private void backToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save Current Question as Parcelable Type
        outState.putParcelable(QUESTION, mQuestion);
        outState.putInt(TIMER, mCurrentTimerValue);
    }

    private Emitter.Listener onPlayListener = args -> runOnUiThread(() -> {
        String state = args[0].toString();
        String sender = args[1].toString();
        String receiver = args[2].toString();
        String message = args[3].toString();

        if (receiver.equals(username)) {
            if(mGameWaitDialog != null)mGameWaitDialog.dismiss();
            final PlayerPreferences playerPreferences = new PlayerPreferences(this);
            switch (state) {
                case Game.ANSWER: {
                    //Update Booleans
                    isOtherPlayerSubmit = true;
                    isOtherPlayerTrue = message.equals(Result.VALID_RESULT);
                    break;
                }
                case Game.NEXT: {
                    //Update Level
                    mGameLevel++;
                    //Return Settings to Default
                    binding.answerSubmit.setClickable(true);
                    isOtherPlayerTrue = false;
                    isOtherPlayerSubmit = false;
                    //Get Question and Update UI
                    binding.answersGroup.clearCheck();
                    Type questionType = new TypeToken<Question>() {
                    }.getType();
                    mQuestion = gson.fromJson(message, questionType);
                    updateGameUI();
                    mCurrentTimerValue = 10;
                    onGameStopTimer();
                    onGameTimeCounter();
                    Toast.makeText(CONTEXT, "Next Round", Toast.LENGTH_SHORT).show();
                    break;
                }
                case Game.END: {
                    switch (message) {
                        case Result.TWO_PLAYERS_WINNER:
                            playerPreferences.playerScoreUp(mGameLevel);
                            GameDialog.showWinnerDialog(CONTEXT, mGameLevel, this::backToMainMenu);
                            break;
                        case Result.TWO_PLAYERS_LOSER:
                            playerPreferences.playerScoreDown(mGameLevel);
                            GameDialog.showLoserDialog(CONTEXT, mGameLevel, this::backToMainMenu);
                            break;
                        case Result.SENDER_PLAYER_WINNER:
                            playerPreferences.playerScoreDown(mGameLevel);
                            GameDialog.showLoserDialog(CONTEXT, mGameLevel, this::backToMainMenu);
                            break;
                        case Result.RECEIVER_PLAYER_WINNER:
                            playerPreferences.playerScoreUp(mGameLevel);
                            GameDialog.showWinnerDialog(CONTEXT, mGameLevel, this::backToMainMenu);
                            break;
                    }
                    break;
                }
            }
        }
    });
}
