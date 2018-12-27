package com.amrdeveloper.fastmind.preferences;

import android.content.Context;

import com.amrdeveloper.fastmind.objects.Player;

/**
 * This Class to manage Player Session Login and Logout
 */
public class Session {

    private PlayerPreferences mPlayerPref;

    public Session(Context context) {
        mPlayerPref = new PlayerPreferences(context);
    }

    /**
     * @param player : Player Information
     * @return : True if this player inserted and login is done
     */
    public boolean playerLogIn(Player player) {
        return mPlayerPref.insertPlayerInformation(player);
    }

    /**
     * @return : True if current player deleted from SharePreferences
     */
    public boolean playerLogOut() {
        return mPlayerPref.deletePlayerInformation();
    }
}
