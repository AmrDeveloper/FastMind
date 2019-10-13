package com.amrdeveloper.fastmind.datasource;

public class DataException extends Exception {
    private Issue issue;

    public DataException(Issue issue, String message) {
        super(message);
        this.issue = issue;
    }

    public boolean shouldRetry() {
        return issue.isShouldRetry();
    }
}

