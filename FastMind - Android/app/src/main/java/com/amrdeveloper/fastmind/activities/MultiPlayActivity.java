package com.amrdeveloper.fastmind.activities;

import android.app.Dialog;
import android.content.Context;
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

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.objects.Question;
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

    //Views
    private TextView mGamePlayersInfo;
    private TextView mGameLevelInfo;
    private TextView mGameQuestionBody;
    private RadioGroup mGameAnswersGroup;
    private TextView mGameTimerCounter;
    private Button mSubmitButton;

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
    private final Context CONTEXT = MultiPlayActivity.this;

    private final static int GAME_TIME = 10;
    private final static String QUESTION = "question";
    private final static String TIMER = "timer";
    private static final String DEBUGGING = SinglePlayActivity.class.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_play);

        initiateViews();
        keepScreenOn();
        onGameActivityStart(savedInstanceState);
        connectToServer();
        getGameInformation();
        updateGameUI();
        onGameTimeCounter();
    }

    private void initiateViews() {
        mGamePlayersInfo = findViewById(R.id.playersInfo);
        mGameLevelInfo = findViewById(R.id.levelInfo);
        mGameQuestionBody = findViewById(R.id.questionBody);
        mGameAnswersGroup = findViewById(R.id.answersGroup);
        mGameTimerCounter = findViewById(R.id.gameTimer);

        mSubmitButton = findViewById(R.id.answerSubmit);
        mSubmitButton.setOnClickListener(view -> onGameSubmitButton());
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
        mGamePlayersInfo.setText(username + " VS " + player);
        mGameLevelInfo.setText("Level : " + mGameLevel);

        //Update Question
        mGameQuestionBody.setText(mQuestion.getQuestionBody());

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

    private void keepScreenOn() {
        mGameTimerCounter.setKeepScreenOn(true);
    }

    private void connectToServer() {
        mGameSocket = GameSocket.getSocket(this);
        //mGameSocket.on("play", onPlayListener);
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
                        mGameTimerCounter.setText(newTile);
                    } else {
                        onGameStopTimer();
                        if(mGameWaitDialog != null)mGameWaitDialog.dismiss();
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

    private Dialog mGameWaitDialog;
    private void onGameSubmitButton() {
        int checkedId = mGameAnswersGroup.getCheckedRadioButtonId();
        if (checkedId != -1) {
            mSubmitButton.setClickable(false);
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
                        mSubmitButton.setClickable(true);
                        mGameAnswersGroup.clearCheck();
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
                    Toast.makeText(this, "Wait", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Wait", Toast.LENGTH_SHORT).show();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save Current Question as Parcelable Type
        outState.putParcelable(QUESTION, mQuestion);
        outState.putInt(TIMER, mCurrentTimerValue);
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

    private Emitter.Listener onPlayListener = args -> runOnUiThread(() -> {
        String state = args[0].toString();
        String sender = args[1].toString();
        String receiver = args[2].toString();
        String message = args[3].toString();

        if (receiver.equals(username)) {
            if(mGameWaitDialog != null)mGameWaitDialog.dismiss();
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
                    mSubmitButton.setClickable(true);
                    isOtherPlayerTrue = false;
                    isOtherPlayerSubmit = false;
                    //Get Question and Update UI
                    mGameAnswersGroup.clearCheck();
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
                            GameDialog.showWinnerDialog(CONTEXT, mGameLevel, this::backToMainMenu);
                            break;
                        case Result.TWO_PLAYERS_LOSER:
                            GameDialog.showLoserDialog(CONTEXT, mGameLevel, this::backToMainMenu);
                            break;
                        case Result.SENDER_PLAYER_WINNER:
                            GameDialog.showLoserDialog(CONTEXT, mGameLevel, this::backToMainMenu);
                            break;
                        case Result.RECEIVER_PLAYER_WINNER:
                            GameDialog.showWinnerDialog(CONTEXT, mGameLevel, this::backToMainMenu);
                            break;
                    }
                    break;
                }
            }
        }
    });
}
