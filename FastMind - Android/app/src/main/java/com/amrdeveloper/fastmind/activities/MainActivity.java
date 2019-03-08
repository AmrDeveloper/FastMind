package com.amrdeveloper.fastmind.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.databinding.ActivityMainBinding;
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
import com.amrdeveloper.fastmind.receiver.NetworkReceiver;
import com.amrdeveloper.fastmind.receiver.OnConnectListener;
import com.amrdeveloper.fastmind.utils.QuestionGenerator;
import com.amrdeveloper.fastmind.utils.SynchronizeUtils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int mCurrentLevel;
    private int mCurrentScore;

    private Player player;
    private Socket mGameSocket;
    private Gson gson = new Gson();
    private NetworkReceiver networkReceiver;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        networkReceiver = new NetworkReceiver(onConnectListener);

        getCurrentPlayerInformation();
        updateUserInformation();
        continueVisibility();
        connectToServer();
        setOptionsActions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getCurrentPlayerInformation();
        updateUserInformation();
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
        String level = String.format(Locale.getDefault(), "Level : %d", mCurrentLevel);
        String score = String.format(Locale.getDefault(), "Score : %d", mCurrentScore);
        int avatarId = Avatar.AVATARS[player.getAvatarID()];

        binding.usernameInfo.setText(username);
        binding.levelInfo.setText(level);
        binding.scoreInfo.setText(score);
        binding.usernameInfo.setCompoundDrawablesWithIntrinsicBounds(avatarId, 0, 0, 0);
    }

    private void getCurrentPlayerInformation() {
        PlayerPreferences playerPreferences = new PlayerPreferences(this);
        player = playerPreferences.queryPlayerInformation();
    }

    private void continueVisibility() {
        if (mCurrentScore == 0 && mCurrentLevel < 2) {
            binding.continueOption.setVisibility(View.GONE);
        } else {
            binding.continueOption.setVisibility(View.VISIBLE);
        }
    }

    private void showOnlineOptions() {
        binding.challengeOption.setVisibility(View.VISIBLE);
        binding.feedOption.setVisibility(View.VISIBLE);
        binding.rankOption.setVisibility(View.VISIBLE);
        binding.settingsOption.setVisibility(View.VISIBLE);
        binding.logoutOption.setVisibility(View.VISIBLE);
    }

    private void hideOnlineOptions() {
        binding.challengeOption.setVisibility(View.GONE);
        binding.feedOption.setVisibility(View.GONE);
        binding.rankOption.setVisibility(View.GONE);
        binding.settingsOption.setVisibility(View.GONE);
        binding.logoutOption.setVisibility(View.GONE);
    }

    private void setOptionsActions() {
        binding.continueOption.setOnClickListener(v -> launchActivity(SinglePlayActivity.class));
        binding.startOption.setOnClickListener(v -> newSingleGameAction());
        binding.challengeOption.setOnClickListener(v -> launchActivity(ChallengeActivity.class));
        binding.feedOption.setOnClickListener(v -> launchActivity(FeedActivity.class));
        binding.rankOption.setOnClickListener(v -> launchActivity(RankActivity.class));
        binding.settingsOption.setOnClickListener(v -> launchActivity(SettingsActivity.class));
        binding.aboutOption.setOnClickListener(v -> launchActivity(AboutActivity.class));
        binding.logoutOption.setOnClickListener(v -> logoutAction());
    }

    public void newSingleGameAction() {
        //Reset Score and level to zero
        PlayerChanger playerChanger = new PlayerChanger(this);
        playerChanger.newGameMode();
        //Go to Single Play Activity
        launchActivity(SinglePlayActivity.class);
        finish();
    }

    private void logoutAction() {
        mGameSocket.disconnect();

        //Update Current User Information To Server
        SynchronizeUtils syncUtils = new SynchronizeUtils(this);
        syncUtils.syncToServer(player);

        //Remove all player local information
        Session session = new Session(this);
        session.playerLogOut();

        launchActivity(LoginActivity.class);
        finish();
    }

    public void launchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
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

    private OnConnectListener onConnectListener = new OnConnectListener() {
        @Override
        public void onConnected() {
            showOnlineOptions();
        }

        @Override
        public void onDisConnected() {
            hideOnlineOptions();
        }
    };
}
