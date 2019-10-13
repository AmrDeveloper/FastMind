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

    private transient Throwable throwable;


    private transient State progress;


    public static Player success(String username, String email, String pass,
                                 int level, int score, int state, int playing,
                                 int winNum, int loseNum, int avatarId) {
        return new Player(username, email, pass, level, score, state, playing, winNum, loseNum, avatarId, State.SUCCESS, null);
    }

    public static Player success(Player player) {
        return new Player(player.getUsername(),
                player.getEmail(),
                player.getPassword(),
                player.getLevel(),
                player.getScore(),
                player.getStateInt(),
                player.getPlayingInt(),
                player.getWinNumber(),
                player.getLoseNumber(),
                player.getAvatarID(), State.SUCCESS, null);
    }

    public static Player error(Throwable throwable) {
        return new Player("", "", "", 0, 0, 0, 0, 0, 0, 0, State.ERROR, throwable);
    }

    public static Player progress() {
        return new Player("", "", "", 0, 0, 0, 0, 0, 0, 0, State.PROGRESS, null);
    }


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

    private Player(String username, String email, String pass,
                   int level, int score, int state, int playing,
                   int winNum, int loseNum, int avatarId, State progress, Throwable throwable) {
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
        this.progress = progress;
        this.throwable = throwable;
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

    public void setScore(int score) {
        this.score = score;
    }

    public int getWinNumber() {
        return winNumber;
    }

    public int getLoseNumber() {
        return loseNumber;
    }

    public int getAvatarID() {
        return avatarID;
    }

    public int getPlaying() {
        return playing;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public State getProgress() {
        return progress;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            Player player = (Player) obj;
            return username.equals(player.getUsername());
        }
        return false;
    }

}
