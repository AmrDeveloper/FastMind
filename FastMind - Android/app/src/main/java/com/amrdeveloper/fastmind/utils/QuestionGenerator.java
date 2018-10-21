package com.amrdeveloper.fastmind.utils;

import com.amrdeveloper.fastmind.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author AmrDeveloper
 */
public class QuestionGenerator {

    private Random random;
    private final String[] operationListStr = {"Addition", "Subtraction", "Division", "Multiplication"};

    public QuestionGenerator() {
        this.random = new Random();
    }

    public Question generateQuestion(int level) {
        int operationIndex = getOperationIndex();
        int numberOne = generateNumber(level);
        int numberTwo = generateNumber(level);
        //Make sure number one not equal number two
        while (numberOne == numberTwo) {
            numberTwo = generateNumber(level);
        }
        String questionTitle = getQuestionBody(operationIndex, numberOne, numberTwo);
        int trueResult = getQuestionResult(operationIndex, numberOne, numberTwo);
        List<String> answers = generateFakeAnswers(trueResult, level);
        return new Question(level,questionTitle, trueResult, answers);
    }

    private int getOperationIndex() {
        return random.nextInt(3);
    }

    private int generateNumber(int level) {
        return random.nextInt(level * 5) + level;
    }

    private List<String> generateFakeAnswers(int trueAnswer, int level) {
        List<String> answers = new ArrayList();
        answers.add(String.valueOf(trueAnswer));
        int wrongResult = 0;
        while (wrongResult != 3) {
            int number = generateNumber(level);
            if (!answers.contains(String.valueOf(number))) {
                answers.add(String.valueOf(number));
                wrongResult++;
            }
        }
        return answers;
    }

    private int getQuestionResult(int operationIndex, int numOne, int numTwo) {
        switch (operationIndex) {
            case 0:
                return numOne + numTwo;
            case 1:
                return numOne - numTwo;
            case 2:
                return numOne * numTwo;
            case 3:
                return numOne / numTwo;
        }
        return -1;
    }

    private String getQuestionBody(int operationIndex, int numOne, int numTwo) {
        switch (operationIndex) {
            case 0:
                return operationListStr[0] + " " + numOne + " and " + numTwo;
            case 1:
                return operationListStr[1] + " " + numOne + " and " + numTwo;
            case 2:
                return operationListStr[2] + " " + numOne + " by " + numTwo;
            case 3:
                return operationListStr[3] + " " + numOne + " by " + numTwo;
            default:
                return "";
        }
    }
}
