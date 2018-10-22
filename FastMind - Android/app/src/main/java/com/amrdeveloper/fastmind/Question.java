package com.amrdeveloper.fastmind;

import java.util.List;

//TODO : Add SerializedName for every var
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

    public int getQuestionLevel() {
        return mQuestionLevel;
    }

    public String getQuestionBody() {
        return mQuestionBody;
    }

    public int getTrueResult() {
        return mTrueResult;
    }

    public List<String> getQuestionAnswers() {
        return mQuestionAnswers;
    }
}
