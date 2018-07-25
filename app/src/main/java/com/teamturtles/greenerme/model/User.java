package com.teamturtles.greenerme.model;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class User {
    public String username;
    public String email;
    public int points;
    public boolean takenQuiz;

    public User() {

    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.points = 0;
        this.takenQuiz = false;
    }

    public int getPoints() {
        return points;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean getTakenQuiz() {
        return takenQuiz;
    }
}