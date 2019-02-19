package com.amrdeveloper.fastmind.activities;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.GridView;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.adapter.AvatarGridAdapter;
import com.amrdeveloper.fastmind.databinding.ActivitySettingsBinding;
import com.amrdeveloper.fastmind.objects.Avatar;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.preferences.PlayerPreferences;
import com.amrdeveloper.fastmind.utils.SynchronizeUtils;

import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {

    private PlayerPreferences preferences;
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        preferences = new PlayerPreferences(this);
        Player player = preferences.queryPlayerInformation();

        binding.playerCurrentAvatar.setImageResource(Avatar.AVATARS[player.getAvatarID()]);
        binding.changeAvatarButton.setOnClickListener(v -> changePlayerAvatar());

    }

    private void changePlayerAvatar() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.avatar_dialog);
        GridView gridView = dialog.findViewById(R.id.avatarsGridView);
        AvatarGridAdapter avatarGridAdapter = new AvatarGridAdapter(this, Arrays.asList(Avatar.AVATARS));
        gridView.setSelected(true);
        gridView.setAdapter(avatarGridAdapter);
        gridView.setOnItemClickListener((parent, view, avatarPosition, id) -> {
            //Update ImageView with current player Avatar
            int avatarId = Avatar.AVATARS[avatarPosition];
            binding.playerCurrentAvatar.setImageResource(avatarId);

            //Update Local Player Avatar Id
            preferences.setPlayerAvatarIndex(avatarPosition);

            //TODO: Update Player Avatar In Server Database

            //Finish Dialog
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
