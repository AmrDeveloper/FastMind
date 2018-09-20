package com.amrdeveloper.fastmind.model;

public class Player {

    private String username;
    private String email;
    private String password;

    private int level;
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
}
