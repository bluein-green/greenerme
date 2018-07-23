package com.teamturtles.greenerme.model;

public class QuizInfo {
    private String startDate;
    private String endDate;
    private String topic;
    private String description;

    public QuizInfo() {}

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
