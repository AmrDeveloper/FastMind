package com.amrdeveloper.fastmind.preferences;

import android.content.Context;

/**
 * Created by AmrDeveloper on 11/3/2018.
 */

public class PlayerChanger {

    private PlayerPreferences mPlayerPref;

    public PlayerChanger(Context context) {
        mPlayerPref = new PlayerPreferences(context);
    }

    /**
     * Reset All changeable information to default information
     */
    public void newGameMode(){
        mPlayerPref.setPlayerLevel(1);
        mPlayerPref.setPlayerScore(0);
        mPlayerPref.setPlayerWinNum(0);
        mPlayerPref.setPlayerLoseNum(0);
    }
}
