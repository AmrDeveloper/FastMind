package com.amrdeveloper.fastmind.preferences;

import android.content.Context;

import com.amrdeveloper.fastmind.Player;

public class Session {

    private PlayerPreferences mPlayerPref;

    public Session(Context context) {
        mPlayerPref = new PlayerPreferences(context);
    }

    public boolean playerLogIn(Player player) {
        return mPlayerPref.insertPlayerInformation(player);
    }

    public boolean playerLogOut() {
        return mPlayerPref.deletePlayerInformation();
    }
}
