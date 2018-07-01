package com.teamturtles.greenerme;

public class QuizQuestions {
    private String[] questions;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Item.class)
    public QuizQuestions() {}

    public void setQuestions(String[] questions) {
        this.questions = questions;
    }

    public String[] getQuestions() {
        return questions;
    }

    public String getQuizQuestion(int qnNum) {
        return questions[qnNum];
    }

    public int getNumQuestions() {
        return questions.length;
    }
}
