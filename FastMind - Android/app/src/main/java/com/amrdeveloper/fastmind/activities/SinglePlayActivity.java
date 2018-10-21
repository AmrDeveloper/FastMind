package com.amrdeveloper.fastmind.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amrdeveloper.fastmind.Question;
import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.utils.QuestionGenerator;

import java.util.List;

public class SinglePlayActivity extends AppCompatActivity {

    private TextView mPlayerLevel;
    private TextView mPlayerScore;
    private TextView mQuestionBody;
    private TextView mGameTimerCounter;
    private RadioGroup mGameAnswersGroup;
    private Button mGameSubmitButton;

    private boolean isGameEnd;

    private int level = 1;
    private String mQuestionTrueAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_play);
        initiateViews();

        updateQuestionUI();
    }

    private void initiateViews(){
        mPlayerLevel = findViewById(R.id.levelInfo);
        mPlayerScore = findViewById(R.id.scoreInfo);

        mQuestionBody = findViewById(R.id.questionBody);
        mGameTimerCounter = findViewById(R.id.gameTimer);

        mGameAnswersGroup = findViewById(R.id.answersGroup);
        mGameSubmitButton = findViewById(R.id.gameSubmit);
    }

    private void updateQuestionUI(){
        //Generate Question
        final QuestionGenerator mQuestionGenerator = new QuestionGenerator();
        Question question = mQuestionGenerator.generateQuestion(level);

        //Update Question
        mQuestionBody.setText(question.getQuestionBody());

        //Update Answers
        List<String> answers = question.getQuestionAnswers();

        //Update UI Radio Buttons
        for(int i = 0 ; i < mGameAnswersGroup.getChildCount() ; i++){
            View item = mGameAnswersGroup.getChildAt(i);
            if(item instanceof RadioButton){
                RadioButton answersRadioButton = (RadioButton) item;
                answersRadioButton.setText(answers.get(i));
            }
        }
    }
}
