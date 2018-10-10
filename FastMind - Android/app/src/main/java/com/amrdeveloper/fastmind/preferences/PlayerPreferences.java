package com.amrdeveloper.fastmind.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.amrdeveloper.fastmind.model.Player;

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
        return new Player(username, email, password, level, score);
    }

    /**
     * Update Local Player Current Score to use it after any Game
     * @param newScore : Player new Score
     * @return : true if edit is done without problem
     */
    public boolean updatePlayerScore(int newScore){
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        editor.putInt(SCORE,newScore);
        return editor.commit();
    }

    /**
     * Update Local Player Current level to use it after any Game
     * @param level : Player new level
     * @return : true if edit is done without problem
     */
    public boolean updatePlayerLevel(int level){
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = playerPreference.edit();
        editor.putInt(LEVEL,level);
        return editor.commit();
    }

    /**
     * This method will help when implements : Logout Option
     * Delete this user Share Preference
     * @return : true if this local player deleted from share preferences
     */
    public boolean deletePlayerInformation() {
        SharedPreferences playerPreference = context.getSharedPreferences(
                PLAYER_PREF_KEY
                , Context.MODE_PRIVATE);

        return playerPreference.edit().clear().commit();
    }
}
