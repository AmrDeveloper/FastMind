package com.amrdeveloper.fastmind.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.objects.Question;
import com.amrdeveloper.fastmind.preferences.PlayerChanger;
import com.amrdeveloper.fastmind.preferences.PlayerPreferences;
import com.amrdeveloper.fastmind.socket.GameSocket;
import com.amrdeveloper.fastmind.utils.GameDialog;
import com.amrdeveloper.fastmind.utils.QuestionGenerator;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {

    private TextView mUsernameInfo;
    private TextView mLevelInfo;
    private TextView mScoreInfo;

    private Button mContinueOption;

    private int mCurrentLevel;
    private int mCurrentScore;

    private Player player;
    private Socket mGameSocket;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiateViews();
        updateUserInformation();
        continueVisibility();
        connectToServer();
    }

    private void initiateViews() {
        mUsernameInfo = findViewById(R.id.usernameInfo);
        mLevelInfo = findViewById(R.id.levelInfo);
        mScoreInfo = findViewById(R.id.scoreInfo);

        mContinueOption = findViewById(R.id.continueOption);
    }

    private void connectToServer() {
        mGameSocket = GameSocket.getSocket(this);
        mGameSocket.connect();
        mGameSocket.emit("username", player.getUsername());
        mGameSocket.on("request", onRequestListener);
        mGameSocket.on("play", onPlayListener);
    }

    private void updateUserInformation() {
        getCurrentPlayerInformation();
        //Update UI
        mUsernameInfo.setText(player.getUsername());
        mLevelInfo.setText("Level : " + player.getLevel());
        mScoreInfo.setText("Score : " + player.getScore());
        //Update Values
        mCurrentLevel = player.getLevel();
        mCurrentScore = player.getScore();
    }

    private void getCurrentPlayerInformation() {
        PlayerPreferences playerPreferences = new PlayerPreferences(this);
        player = playerPreferences.queryPlayerInformation();
    }

    private void continueVisibility() {
        if (mCurrentScore == 0 && mCurrentLevel == 0) {
            mContinueOption.setVisibility(View.GONE);
        } else {
            mContinueOption.setVisibility(View.VISIBLE);
        }
    }

    public void newSingleGame(View view) {
        //Reset Score and level to zero
        PlayerChanger playerChanger = new PlayerChanger(this);
        playerChanger.newGameMode();
        //Go to Single Play Activity
        Intent intent = new Intent(this, SinglePlayActivity.class);
        startActivity(intent);
        finish();
    }

    public void continueSingleGame(View view) {
        Intent intent = new Intent(this, SinglePlayActivity.class);
        startActivity(intent);
    }

    public void challengeActivity(View view) {
        Intent intent = new Intent(this, ChallengeActivity.class);
        startActivity(intent);
        finish();
    }

    public void feedActivity(View view) {
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }

    public void rankActivity(View view) {
        Intent intent = new Intent(this, RankActivity.class);
        startActivity(intent);
    }

    public void aboutActivity(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void logoutAction(View view) {
        //TODO : Update Current User Information To Server
        PlayerPreferences preferences = new PlayerPreferences(this);
        preferences.deletePlayerInformation();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private Emitter.Listener onRequestListener = args -> runOnUiThread(() -> {
        final String state = args[0].toString();
        final String sender = args[1].toString();
        final String receiver = args[2].toString();

        if (receiver.equals(player.getUsername())) {
            switch (state) {
                case "receive": {
                    GameDialog.showRequestDialog(this, sender,
                            () -> mGameSocket.emit("request", "accept", sender, receiver)
                            , () -> mGameSocket.emit("request", "refuse", sender, receiver));
                    break;
                }
                case "accept": {
                    QuestionGenerator generator = new QuestionGenerator();
                    Question questionObj = generator.generateQuestion(player.getLevel());

                    mGameSocket.emit("play", "start", receiver, sender, gson.toJson(questionObj));

                    Intent intent = new Intent(this, MultiPlayActivity.class);
                    intent.putExtra("sender", sender);
                    intent.putExtra("receiver", receiver);
                    intent.putExtra("question", questionObj);
                    startActivity(intent);

                    mGameSocket.off("request");
                    finish();
                    break;
                }
                case "refuse": {
                    mGameSocket.emit("request", "refuse", sender, receiver);
                    break;
                }
            }
        }
    });

    private Emitter.Listener onPlayListener = args -> runOnUiThread(() -> {
        final String state = args[0].toString();
        final String sender = args[1].toString();
        final String receiver = args[2].toString();
        final String question = args[3].toString();

        if (receiver.equals(player.getUsername())) {
            if (state.equals("start")) {
                //Convert Question From JSON to Object
                Type questionType = new TypeToken<Question>() {
                }.getType();
                Question questionObj = gson.fromJson(question, questionType);

                //Go to MultiPlayActivity with sender and receiver and question
                Intent intent = new Intent(this, MultiPlayActivity.class);
                intent.putExtra("sender", sender);
                intent.putExtra("receiver", receiver);
                intent.putExtra("question", questionObj);
                startActivity(intent);

                mGameSocket.off("request");
                finish();
            }
        }
    });
}
