package com.amrdeveloper.fastmind.objects;

import com.google.gson.annotations.SerializedName;

public class Feed {

    @SerializedName("player1")
    private String mFirstPlayer;

    @SerializedName("player2")
    private String mSecondPlayer;

    @SuppressWarnings("result")
    private int mGameResult;

    @SerializedName("score")
    private String mGameScore;

    public Feed(String player1, String player2,int result, String score) {
        mFirstPlayer = player1;
        mSecondPlayer = player2;
        mGameResult = result;
        mGameScore = score;
    }

    public String getFirstPlayer(){
        return mFirstPlayer;
    }

    public String getSecondPlayer(){
        return mSecondPlayer;
    }

    public String getGameScore(){
        return mGameScore;
    }

    public int getGameResult(){
        return mGameResult;
    }
}
