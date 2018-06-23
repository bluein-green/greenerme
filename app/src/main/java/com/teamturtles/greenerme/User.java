package com.teamturtles.greenerme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class User {
    public String username;
    public String email;
    public int num_points;

    public User() {

    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        num_points = 0;
    }

    public int getPoints() {
        return num_points;
    }

    public void addPoints(int pointsToBeAdded) {
        num_points += pointsToBeAdded;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}