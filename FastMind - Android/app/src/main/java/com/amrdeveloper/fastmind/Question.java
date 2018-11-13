package com.amrdeveloper.fastmind;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {

    @SerializedName("level")
    private int mQuestionLevel;

    @SerializedName("body")
    private String mQuestionBody;

    @SerializedName("trueResult")
    private int mTrueResult;

    @SerializedName("answers")
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
