package com.teamturtles.greenerme;

public class QuizChoices {
    private String[][] choices;

    public QuizChoices() {}

    public void setChoices(String[][] choices) {
        this.choices = choices;
    }

    public String[][] getChoices() {
        return choices;
    }

    public String getChoice1(int qnNum) {
        return choices[qnNum][0];
    }

    public String getChoice2(int qnNum) {
        return choices[qnNum][1];
    }

    public String getChoice3(int qnNum) {
        return choices[qnNum][2];
    }

    public String getChoice4(int qnNum) {
        return choices[qnNum][3];
    }
}
