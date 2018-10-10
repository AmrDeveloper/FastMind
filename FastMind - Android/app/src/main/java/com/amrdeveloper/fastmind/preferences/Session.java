package com.amrdeveloper.fastmind.preferences;

import android.content.Context;

import com.amrdeveloper.fastmind.model.Player;


public class Session {

    private Context mContext;
    private PlayerPreferences mPlayerPref;

    public Session(Context context) {
        mContext = context;
        mPlayerPref = new PlayerPreferences(context);
    }

    public boolean playerLogIn(Player player) {
        return mPlayerPref.insertPlayerInformation(player);
    }

    public boolean playerLogOut() {
        return mPlayerPref.deletePlayerInformation();
    }
}
