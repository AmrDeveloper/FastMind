package com.amrdeveloper.fastmind.datasource;

public enum Issue {
    NETWORK(true), SERVER(false), API(false);

    private final boolean shouldRetry;

    Issue(boolean shouldRetry) {
        this.shouldRetry = shouldRetry;
    }

    public boolean isShouldRetry() {
        return shouldRetry;
    }
}
