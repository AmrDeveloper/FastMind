package com.amrdeveloper.fastmind.objects;

import com.google.gson.annotations.SerializedName;

public class Player {

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("level")
    private int level;

    @SerializedName("score")
    private int score;

    @SerializedName("online")
    private int state;

    @SerializedName("playing")
    private int playing;

    @SerializedName("winNum")
    private int winNumber;

    @SerializedName("loseNum")
    private int loseNumber;

    @SerializedName("avatarID")
    private int avatarID;

    public Player(String username, String email, String password, int level, int score) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.level = level;
        this.score = score;
        this.state = 1;
        this.playing = 0;
        this.winNumber = 0;
        this.loseNumber = 0;
        this.avatarID = 0;
    }

    public Player(String username, String email, String pass,
                  int level, int score, int state, int playing,
                  int winNum, int loseNum, int avatarId) {
        this.username = username;
        this.email = email;
        this.password = pass;
        this.level = level;
        this.score = score;
        this.state = state;
        this.playing = playing;
        this.winNumber = winNum;
        this.winNumber = loseNum;
        this.avatarID = avatarId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public String getState() {
        return (state == 0) ? "Offline" : "Online";
    }

    public int getStateInt() {
        return state;
    }

    public int getPlayingInt() {
        return playing;
    }

    public boolean isPlaying() {
        return (playing == 1);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getWinNumber() {
        return winNumber;
    }

    public void setWinNumber(int winNumber) {
        this.winNumber = winNumber;
    }

    public int getLoseNumber() {
        return loseNumber;
    }

    public void setLoseNumber(int loseNumber) {
        this.loseNumber = loseNumber;
    }

    public int getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(int avatarID) {
        this.avatarID = avatarID;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Player){
            Player player = (Player)obj;
            return username.equals(player.getUsername());
        }
        return false;
    }
}
