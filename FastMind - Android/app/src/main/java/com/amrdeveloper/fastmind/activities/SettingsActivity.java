package com.amrdeveloper.fastmind.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.adapter.AvatarGridAdapter;
import com.amrdeveloper.fastmind.databinding.ActivitySettingsBinding;
import com.amrdeveloper.fastmind.objects.Avatar;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.preferences.PlayerPreferences;
import com.amrdeveloper.fastmind.receiver.NetworkReceiver;
import com.amrdeveloper.fastmind.receiver.OnConnectListener;
import com.amrdeveloper.fastmind.utils.DataValidation;
import com.amrdeveloper.fastmind.utils.PlayerUpdateUtils;

import java.util.Arrays;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private Player player;
    private PlayerPreferences updatePreferenceUtils;
    private PlayerUpdateUtils updateServerUtils;

    private NetworkReceiver networkReceiver;

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        updateServerUtils = new PlayerUpdateUtils(this);
        updatePreferenceUtils = new PlayerPreferences(this);
        networkReceiver = new NetworkReceiver(onConnectListener);

        player = updatePreferenceUtils.queryPlayerInformation();

        binding.playerCurrentAvatar.setImageResource(Avatar.AVATARS[player.getAvatarID()]);

        binding.changeAvatarButton.setOnClickListener(v -> changePlayerAvatar());
        binding.changeUsernameButton.setOnClickListener(v -> changePlayerUsername());
        binding.changeEmailButton.setOnClickListener(v -> changePlayerEmail());
        binding.changePassButton.setOnClickListener(v -> changePlayerPassword());
        binding.deleteAccountButton.setOnClickListener(v -> deletePlayerAccount());
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

    private void changePlayerAvatar() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_avatar_dialog);
        GridView gridView = dialog.findViewById(R.id.avatarsGridView);
        AvatarGridAdapter avatarGridAdapter = new AvatarGridAdapter(this, Arrays.asList(Avatar.AVATARS));
        gridView.setSelected(true);
        gridView.setAdapter(avatarGridAdapter);
        gridView.setOnItemClickListener((parent, view, avatarPosition, id) -> {
            //Update ImageView with current player Avatar
            int avatarId = Avatar.AVATARS[avatarPosition];
            binding.playerCurrentAvatar.setImageResource(avatarId);

            //Update Local Player Avatar Id
            updatePreferenceUtils.setPlayerAvatarIndex(avatarPosition);

            //Update Server Player Avatar Id
            updateServerUtils.updateAvatar(player.getEmail(), avatarPosition);

            //Finish Dialog
            dialog.dismiss();
        });
        dialog.show();
    }

    private void changePlayerUsername() {

    }

    private void changePlayerEmail() {

    }

    private void changePlayerPassword() {

    }

    private void deletePlayerAccount() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_account_dialog);

        TextInputEditText passWordEditText = dialog.findViewById(R.id.passWordEditText);
        TextInputLayout passWordInputLayout = dialog.findViewById(R.id.passWordInputLayout);
        Button deleteAccount = dialog.findViewById(R.id.deleteAccount);

        deleteAccount.setOnClickListener(v -> {
            String viewPassword = Objects.requireNonNull(passWordEditText.getText()).toString().trim();
            String truePassword = player.getPassword();

            if (TextUtils.equals(viewPassword, truePassword)
                    && DataValidation.isPasswordValid(viewPassword)) {

                updateServerUtils.deleteAccount(player.getEmail());
                updatePreferenceUtils.deletePlayerInformation();

                dialog.dismiss();

                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);

                finish();
            } else {
                dialog.dismiss();
                passWordInputLayout.setError(getString(R.string.invalid_password));
            }
        });
        dialog.show();
    }

    private OnConnectListener onConnectListener = new OnConnectListener() {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisConnected() {

        }
    };
}
