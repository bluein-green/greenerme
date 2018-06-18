package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage_loggedout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_loggedout);

        Toast.makeText(this, "Hi There! Welcome in!", Toast.LENGTH_SHORT).show();

        TextView signup_btn = (TextView) findViewById(R.id.signupText_btn);

        signup_btn.setOnClickListener(new View.OnClickListener() { // To click to the createe account page
            @Override
            public void onClick(View view) { // To click to the create account page
                Intent signup_intent = new Intent(HomePage_loggedout.this, CreateAccPage.class);
                HomePage_loggedout.this.startActivity(signup_intent);
            }
        });
    }

    public void homepageToLogin(View view) {
        Intent startIntent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(startIntent);
    }
}
