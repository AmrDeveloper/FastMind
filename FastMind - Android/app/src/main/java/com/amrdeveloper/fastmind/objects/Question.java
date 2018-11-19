package com.amrdeveloper.fastmind.objects;

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

    public Question(int level,
                    String body,
                    int trueResult,
                    List<String> answers) {
        this.mQuestionLevel = level;
        this.mQuestionBody = body;
        this.mTrueResult = trueResult;
        this.mQuestionAnswers = answers;
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
