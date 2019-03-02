package com.amrdeveloper.fastmind.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.databinding.ActivityProfileBinding;
import com.amrdeveloper.fastmind.objects.Avatar;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.socket.Game;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class ProfileActivity extends AppCompatActivity {

    private String playerUsername;
    private ActivityProfileBinding binding;

    private RequestQueue queue;
    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        queue = Volley.newRequestQueue(this);

        getCurrentPlayerUsername();
        getCurrentPlayerInformation();
    }

    private void bindPlayerOnUI(Player player) {
        binding.playerUsername.setText(player.getUsername());
        binding.playerScore.setText("Score : " + player.getScore());
        binding.playerLevel.setText("Level : " + player.getLevel());
        binding.playerWinTimes.setText("Win : " + player.getWinNumber());
        binding.playerLoseTimes.setText("Lose : " + player.getLoseNumber());
        binding.playerAvatarImg.setImageResource(Avatar.AVATARS[player.getAvatarID()]);
    }

    private void getCurrentPlayerUsername() {
        Intent intent = getIntent();
        playerUsername = intent.getStringExtra(Game.USERNAME);
    }

    private String generateRequestUrl() {
        String router = "/api/player";
        String requestUrl = getString(R.string.LOCALHOST) + getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("username", playerUsername);
        Log.v("Query",uriBuilder.toString());
        return uriBuilder.toString();
    }

    private void getCurrentPlayerInformation() {
        String requestUrl = generateRequestUrl();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                requestUrl,
                response -> {
                    Player player = gson.fromJson(response, Player.class);
                    bindPlayerOnUI(player);
                },
                error -> {
                    Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show();
                }
        );
        queue.add(request);
    }
}
