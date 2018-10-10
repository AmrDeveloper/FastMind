package com.amrdeveloper.fastmind.utils;

import com.amrdeveloper.fastmind.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {

    //List of Math Operation
    private final char[] operationList = {'+', '-', '*', '/'};

    //List of Math operation as String
    private final String[] operationListStr = {"Addition", "Subtraction", "Division", "Multiplication"};

    //Random Object
    private final Random random = new Random();

    public QuestionGenerator() {

    }

    public Question generateNewQuestion(int questionLevel) {
        int operatorIndex = getOperationIndex();
        int number1 = generateNumInRange(questionLevel, questionLevel * 5);
        int number2 = generateNumInRange(questionLevel, number1);

        String questionBody = getQuestionBody(number1, number2, operatorIndex);
        int trueResult = getQuestionTrueAnswer(number1, number2, operatorIndex);

        List<String> answersList = getShuffleAnswersList(trueResult);

        return new Question(questionLevel,questionBody,trueResult,answersList);
    }

    private int generateNumInRange(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private int getOperationIndex() {
        return generateNumInRange(0, 3);
    }

    private String getQuestionBody(int num1, int num2, int operator) {
        if (operator < 2) {
            return operationListStr[operator] + " " + num1 + " and " + num2;
        } else {
            return operationListStr[operator] + " " + num1 + " by " + num2;
        }
    }

    private int getQuestionTrueAnswer(int num1, int num2, int operator) {
        switch (operator) {
            case 0:
                return num1 + num2;
            case 1:
                return num1 - num2;
            case 2:
                return num1 * num2;
            case 3:
                while (num1 % num2 != 0) {
                    num2 = generateNumInRange(1, num1);
                }
                return num1 / num2;
            default:
                return -1;
        }
    }

    private List<String> getShuffleAnswersList(int trueResult) {
        List<String> answersList = new ArrayList<>();
        answersList.add(String.valueOf(trueResult));

        while (answersList.size() < 3) {
            int generateNum = random.nextInt(trueResult) + 1;
            if (!answersList.contains(String.valueOf(generateNum))) {
                answersList.add(String.valueOf(generateNum));
            }
        }

        Collections.shuffle(answersList);
        return answersList;
    }
}
