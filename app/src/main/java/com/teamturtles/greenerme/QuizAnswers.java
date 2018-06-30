package com.teamturtles.greenerme;

public class QuizAnswers {
    private int [] correctAnswers;

    public QuizAnswers() {}

    public void setCorrectAnswers(int[] correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int[] getCorrectAnswers() {
        return correctAnswers;
    }

    public int getCorrectAnswer(int qnNum) {
        return correctAnswers[qnNum];
    }
}
