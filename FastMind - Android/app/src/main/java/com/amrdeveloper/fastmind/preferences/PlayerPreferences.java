package com.amrdeveloper.fastmind.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.amrdeveloper.fastmind.objects.Player;

public class PlayerPreferences {

    private Context context;

    //Player SharePreference Name
    private static final String PLAYER_PREF_KEY = "PLAYER";

    //Player Object Contract
    private static final String USERNAME = "USERNAME";
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static final String LEVEL = "LEVEL";
    private static final String SCORE = "SCORE";
    private static final String STATE = "STATE";
    private static final String PLAYING = "PLAY";
    private static final String WIN_NUM = "WIN";
    private static final String LOST_NUM = "LOST";
    private static final String AVATAR = "AVATAR";

    public PlayerPreferences(Context context) {
        this.context = context;
    }

    /**
     * @param player : Local Player all information inside Player Object
     * @return : true if local player information inserted into share preference
     */
    public boolean insertPlayerInformation(Player player) {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        editor.putString(USERNAME, player.getUsername());
        editor.putString(EMAIL, player.getEmail());
        editor.putString(PASSWORD, player.getPassword());

        editor.putInt(LEVEL, player.getLevel());
        editor.putInt(SCORE, player.getScore());
        editor.putInt(STATE, player.getStateInt());
        editor.putInt(PLAYING, player.getPlayingInt());

        editor.putInt(WIN_NUM, player.getWinNumber());
        editor.putInt(LOST_NUM, player.getLoseNumber());
        editor.putInt(AVATAR, player.getAvatarID());
        return editor.commit();
    }

    /**
     * @return : Player object contain local player all information
     */
    public Player queryPlayerInformation() {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);

        String username = playerPreference.getString(USERNAME, "");
        String email = playerPreference.getString(EMAIL, "");
        String password = playerPreference.getString(PASSWORD, "");
        int level = playerPreference.getInt(LEVEL, 0);
        int score = playerPreference.getInt(SCORE, 0);
        int state = playerPreference.getInt(STATE, 0);
        int playing = playerPreference.getInt(PLAYING, 0);
        int win = playerPreference.getInt(WIN_NUM, 0);
        int lose = playerPreference.getInt(LOST_NUM, 0);
        int avatarId = playerPreference.getInt(AVATAR, 0);
        return new Player(username, email, password, level, score, state, playing, win, lose, avatarId);
    }

    /**
     * @return : True if username not equal null that mean there is player here
     */
    public boolean isPlayerLogined() {
        Player player = queryPlayerInformation();
        return !player.getUsername().equals("");
    }

    /**
     * Update Local Player Current Score to use it after any Game
     *
     * @param newScore : Player new Score
     * @return : true if edit is done without problem
     */
    public boolean setPlayerScore(int newScore) {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        editor.putInt(SCORE, newScore);
        return editor.commit();
    }

    /**
     * @param points : Add This Score to current Score
     * @return : True if update is done
     */
    public boolean playerScoreUp(int points) {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        int currentScore = playerPreference.getInt(SCORE, 0);
        int netScore = currentScore + points;
        editor.putInt(SCORE, netScore);
        return editor.commit();
    }

    /**
     * @param points : Sub Score from current score
     * @return : True if update is Done
     */
    public boolean playerScoreDown(int points) {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        int currentScore = playerPreference.getInt(SCORE, 0);
        int newScore = currentScore - points;
        editor.putInt(SCORE, newScore);
        return editor.commit();
    }

    /**
     * @param points : Add This point to number of win game
     * @return : True if update is done
     */
    public boolean playerWinNumUp(int points) {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        int currentScore = playerPreference.getInt(WIN_NUM, 0);
        int netScore = currentScore + points;
        editor.putInt(WIN_NUM, netScore);
        return editor.commit();
    }

    /**
     * @param points : Add This point to number of lose game
     * @return : True if update is Done
     */
    public boolean playerLoseNumUp(int points) {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        int currentScore = playerPreference.getInt(LOST_NUM, 0);
        int newScore = currentScore + points;
        editor.putInt(LOST_NUM, newScore);
        return editor.commit();
    }

    /**
     * Update Local Player Current level to use it after any Game
     *
     * @param level : Player new level
     * @return : true if edit is done without problem
     */
    public boolean setPlayerLevel(int level) {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        editor.putInt(LEVEL, level);
        return editor.commit();
    }

    /**
     * Update Local Player Lose game Number
     *
     * @param number : Avatar Id
     * @return true if edit is done without problems
     */
    public boolean setPlayerWinNum(int number) {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        editor.putInt(WIN_NUM, number);
        return editor.commit();
    }

    /**
     * Update Local Player Win game Number
     *
     * @param number : Avatar Id
     * @return true if edit is done without problems
     */
    public boolean setPlayerLoseNum(int number) {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        editor.putInt(LOST_NUM, number);
        return editor.commit();
    }

    /**
     * Update Local Player Avatar Index
     * @param avatarIndex : Avatar index in avatars array
     * @return true if edit is done without problems
     */
    public boolean setPlayerAvatarIndex(int avatarIndex) {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        editor.putInt(AVATAR, avatarIndex);
        return editor.commit();
    }

    /**
     * This method will help when implements : Logout Option
     * Delete this user Share Preference
     *
     * @return : true if this local player deleted from share preferences
     */
    public boolean deletePlayerInformation() {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);

        return playerPreference.edit().clear().commit();
    }

    /**
     * @param player : Player With Current Information from server
     * @return : true if update level and score is done without errors
     */
    public boolean updatePlayerInformation(Player player) {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        editor.putInt(LEVEL, player.getLevel());
        editor.putInt(SCORE, player.getScore());
        return editor.commit();
    }
}
