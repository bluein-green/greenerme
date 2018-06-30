package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage_loggedout extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private Button login_btn;
    private TextView signupText_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_loggedout);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage_loggedin.class));
        }

        login_btn = (Button) findViewById(R.id.login_btn);
        signupText_btn = (TextView) findViewById(R.id.signupText_btn);

        login_btn.setOnClickListener(this);
        signupText_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == login_btn ) {
            startActivity(new Intent(this, LoginPage.class));
        }
        if (view == signupText_btn) {
            startActivity(new Intent(this, CreateAccPage.class));
        }
    }
}
