package com.teamturtles.greenerme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class User {
    private String username;
    private String email;
    private int num_points;

    public Item(String name, String address) {
        username = name;
        email = address;
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