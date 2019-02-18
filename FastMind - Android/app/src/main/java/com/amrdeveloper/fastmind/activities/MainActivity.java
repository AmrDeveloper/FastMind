package com.amrdeveloper.fastmind.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.objects.Avatar;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.objects.Question;
import com.amrdeveloper.fastmind.preferences.PlayerChanger;
import com.amrdeveloper.fastmind.preferences.PlayerPreferences;
import com.amrdeveloper.fastmind.preferences.Session;
import com.amrdeveloper.fastmind.socket.Challenge;
import com.amrdeveloper.fastmind.socket.Game;
import com.amrdeveloper.fastmind.socket.GameSocket;
import com.amrdeveloper.fastmind.utils.GameDialog;
import com.amrdeveloper.fastmind.utils.QuestionGenerator;
import com.amrdeveloper.fastmind.utils.SynchronizeUtils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Locale;

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
        getCurrentPlayerInformation();
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
        mGameSocket.emit(Game.USERNAME, player.getUsername());
        mGameSocket.on(Challenge.REQUEST, onRequestListener);
        mGameSocket.on(Game.PLAY, onPlayListener);
    }

    private void updateUserInformation() {
        //Update Values
        mCurrentLevel = player.getLevel();
        mCurrentScore = player.getScore();

        String username = player.getUsername();
        String level = String.format(Locale.getDefault(),"Level : %d",mCurrentLevel);
        String score = String.format(Locale.getDefault(),"Score : %d",mCurrentScore);
        int avatarId = Avatar.AVATARS[player.getAvatarID()];

        //Update UI
        mUsernameInfo.setText(username);

        mLevelInfo.setText(level);
        mScoreInfo.setText(score);
        mUsernameInfo.setCompoundDrawablesWithIntrinsicBounds(avatarId, 0, 0, 0);
    }

    private void getCurrentPlayerInformation() {
        PlayerPreferences playerPreferences = new PlayerPreferences(this);
        player = playerPreferences.queryPlayerInformation();
    }

    private void continueVisibility() {
        if (mCurrentScore == 0 && mCurrentLevel < 2) {
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

    public void settingsActivity(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void logoutAction(View view) {
        mGameSocket.disconnect();

        //Update Current User Information To Server
        SynchronizeUtils syncUtils = new SynchronizeUtils(this);
        syncUtils.syncToServer(player);

        //Remove all player local information
        Session session = new Session(this);
        session.playerLogOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startMultiPlayerGame(String sender, String receiver, Question question) {
        Intent intent = new Intent(this, MultiPlayActivity.class);
        intent.putExtra(Game.SENDER, sender);
        intent.putExtra(Game.RECEIVER, receiver);
        intent.putExtra(Game.QUESTION, question);
        startActivity(intent);
    }

    private Emitter.Listener onRequestListener = args -> runOnUiThread(() -> {
        final String state = args[0].toString();
        final String sender = args[1].toString();
        final String receiver = args[2].toString();

        if (receiver.equals(player.getUsername())) {
            switch (state) {
                case Challenge.RECEIVE: {
                    if (!MainActivity.this.isFinishing()) {
                        GameDialog.showRequestDialog(MainActivity.this, sender,
                                () -> mGameSocket.emit(Challenge.REQUEST, Challenge.ACCEPT, sender, receiver)
                                , () -> mGameSocket.emit(Challenge.REQUEST, Challenge.REFUSE, sender, receiver));
                    }
                    break;
                }

                case Challenge.ACCEPT: {
                    QuestionGenerator generator = new QuestionGenerator();
                    Question questionObj = generator.generateQuestion(player.getLevel());
                    mGameSocket.emit(Game.PLAY, Game.START, receiver, sender, gson.toJson(questionObj));
                    startMultiPlayerGame(sender, receiver, questionObj);
                    mGameSocket.off(Challenge.REQUEST);
                    finish();
                    break;
                }
                case Challenge.REFUSE: {
                    mGameSocket.emit(Challenge.REQUEST, Challenge.REFUSE, sender, receiver);
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
            if (state.equals(Game.START)) {
                //Convert Question From JSON to Object
                Type questionType = new TypeToken<Question>() {
                }.getType();
                Question questionObj = gson.fromJson(question, questionType);

                //Go to MultiPlayActivity with sender and receiver and question
                startMultiPlayerGame(sender, receiver, questionObj);

                mGameSocket.off(Challenge.REQUEST);
                finish();
            }
        }
    });
}
