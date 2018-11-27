package com.amrdeveloper.fastmind.objects;

import com.google.gson.annotations.SerializedName;

public class Feed {

    @SerializedName("winner")
    private String mWinnerUser;

    @SerializedName("loser")
    private String mLoserUser;

    @SerializedName("level")
    private String mGameLevel;

    public Feed(String winner, String loser, String level) {
        mWinnerUser = winner;
        mLoserUser = loser;
        mGameLevel = level;
    }

    public String getWinnerName(){
        return mWinnerUser;
    }

    public String getLoserName(){
        return mLoserUser;
    }

    public String getGamelevel(){
        return mGameLevel;
    }
}
