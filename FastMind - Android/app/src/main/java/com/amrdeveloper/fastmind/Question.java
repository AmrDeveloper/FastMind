package com.amrdeveloper.fastmind;

import java.util.List;

public class Question {

    private int mQuestionLevel;

    private String mQuestionBody;
    private int mTrueResult;

    private List<String> mQuestionAnswers;

    public Question(int mQuestionLevel,
                    String mQuestionBody,
                    int mTrueResult,
                    List<String> mQuestionAnswers) {
        this.mQuestionLevel = mQuestionLevel;
        this.mQuestionBody = mQuestionBody;
        this.mTrueResult = mTrueResult;
        this.mQuestionAnswers = mQuestionAnswers;
    }

    public int getmQuestionLevel() {
        return mQuestionLevel;
    }

    public String getmQuestionBody() {
        return mQuestionBody;
    }

    public int getmTrueResult() {
        return mTrueResult;
    }

    public List<String> getmQuestionAnswers() {
        return mQuestionAnswers;
    }
}
