package com.teamturtles.greenerme;

import java.util.ArrayList;

public class QuizChoices {
    private ArrayList<ArrayList<String>> choices;

    public QuizChoices() {}

    public void setChoices(ArrayList<ArrayList<String>> choices) {
        this.choices = choices;
    }

    public ArrayList<ArrayList<String>> getChoices() {
        return choices;
    }

    public String getChoice1(int qnNum) {
        return choices.get(qnNum).get(0);
    }

    public String getChoice2(int qnNum) {
        return choices.get(qnNum).get(1);
    }

    public String getChoice3(int qnNum) {
        return choices.get(qnNum).get(2);
    }

    public String getChoice4(int qnNum) {
        return choices.get(qnNum).get(3);
    }
}
