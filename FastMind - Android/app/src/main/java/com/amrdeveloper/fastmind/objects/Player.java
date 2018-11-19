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

    public Player(String username, String email, String password, int level, int score) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.level = level;
        this.score = score;
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

    @Override
    public String toString() {
        return username;
    }
}
