package com.teamturtles.greenerme;

public interface MyQuizCallback {
    void onCallback(QuizQuestions value);

    void onCallback(QuizChoices value);

    void onCallback(QuizAnswers value);
}
