package com.teamturtles.greenerme.model;

import java.util.ArrayList;

public class QuizMaterials {
    private String question;
    private ArrayList<String> choices;
    private int answer;
    private String explanation;

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

    public String getAnswerText() {
        return choices.get(getAnswer());
    }

    public String getExplanation() { return explanation; }

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

    public void setExplanation(String explanation) {this.explanation = explanation; }


    public String getChoice(int choiceNum) {
        return choices.get(choiceNum);
    }

    // get number of choices
    public int getNumChoices() {
        return choices.size();
    }
}
