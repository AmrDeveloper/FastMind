package com.amrdeveloper.fastmind;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question implements Parcelable{

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

    private Question(Parcel in) {
        mQuestionLevel = in.readInt();
        mQuestionBody = in.readString();
        mTrueResult = in.readInt();
        mQuestionAnswers = in.createStringArrayList();
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mQuestionLevel);
        dest.writeString(mQuestionBody);
        dest.writeInt(mTrueResult);
        dest.writeStringList(mQuestionAnswers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
