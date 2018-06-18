package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage_loggedout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_loggedout);

        Toast.makeText(this, "Hi There! Welcome in!", Toast.LENGTH_SHORT).show();

        final TextView signup_btn = (TextView) findViewById(R.id.signup_btn);

        signup_btn.setOnClickListener(new View.OnClickListener() { // To click to the createe account page
            @Override
            public void onClick(View view) {
                Intent signup_intent = new Intent(HomePage_loggedout.this, CreateAccPage.class);
                HomePage_loggedout.this.startActivity(signup_intent);
            }
        });
    }



    /*public void createAccount(View view) { // To click to the create account page
        Intent startIntent = new Intent(getApplicationContext(), CreateAccPage.class);
        startActivity(startIntent);
    }
    */

    public void loginAccount(View view) {
        Intent startIntent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(startIntent);
    }
}
