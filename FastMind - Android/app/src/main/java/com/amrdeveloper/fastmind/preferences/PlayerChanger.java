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

    public void newGameMode(){
        mPlayerPref.setPlayerLevel(1);
        mPlayerPref.setPlayerScore(0);
    }
}
