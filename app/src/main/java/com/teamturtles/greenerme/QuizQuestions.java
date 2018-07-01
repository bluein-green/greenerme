package com.teamturtles.greenerme;

import java.util.ArrayList;

public class QuizQuestions {
    private ArrayList<String> questions;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Item.class)
    public QuizQuestions() {}

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public String getQuizQuestion(int qnNum) {
        return questions.get(qnNum);
    }

    public int getNumQuestions() {
        return questions.size();
    }

}
