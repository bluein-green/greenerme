package com.teamturtles.greenerme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class HomePage_loggedin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_loggedin);

        Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
    }
}
