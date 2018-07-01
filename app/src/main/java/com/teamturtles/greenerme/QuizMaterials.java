package com.teamturtles.greenerme;

import java.util.ArrayList;

public class QuizMaterials {
    private String question;
    private ArrayList<String> choices;
    private int answer;

    public QuizMaterials() {}

    // public getters
    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public int getAnswer() {
        return answer;
    }

    // public setters
    public void setQuestion(String question) {
        this.question = question;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    // individual choice getter
    public String getChoice1() {
        return choices.get(0);
    }

    public String getChoice2() {
        return choices.get(1);
    }

    public String getChoice3() {
        return choices.get(2);
    }

    public String getChoice4() {
        return choices.get(3);
    }


    // get number of choices
    public int getNumChoices() {
        return choices.size();
    }
}
