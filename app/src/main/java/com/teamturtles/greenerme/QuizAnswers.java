package com.teamturtles.greenerme;

import java.util.ArrayList;

public class QuizAnswers {
    private ArrayList<Integer> correctAnswers;

    public QuizAnswers() {}

    public void setCorrectAnswers(ArrayList<Integer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public ArrayList<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public int getCorrectAnswer(int qnNum) {
        return correctAnswers.get(qnNum);
    }
}
