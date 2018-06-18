package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccPage extends AppCompatActivity {

    ProgressBar progressBar;
    EditText username_signup, password_signup, email_signup;
    Button signup_btn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc_page);

        username_signup = (EditText) findViewById(R.id.username_signup);
        password_signup = (EditText) findViewById(R.id.password_signup);
        email_signup = (EditText) findViewById(R.id.email_signup);
        signup_btn = (Button) findViewById(R.id.signup_btn);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        TextView loginText_btn = (TextView) findViewById(R.id.loginText_btn);

        loginText_btn.setOnClickListener(new View.OnClickListener() { // To click to the createe account page
            @Override
            public void onClick(View view) { // To click to the create account page
                Intent login_intent = new Intent(CreateAccPage.this, LoginPage.class);
                CreateAccPage.this.startActivity(login_intent);
            }
        });
    }

    private void registerUser() {
        String username = username_signup.getText().toString().trim();
        String password = password_signup.getText().toString().trim();
        String email = email_signup.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || password.length() < 6 || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (username.isEmpty()) {
                username_signup.setError("Email is required");
                username_signup.requestFocus();
            }

            if (password.isEmpty()) {
                password_signup.setError("Password is required");
                password_signup.requestFocus();
            }

            if (password.length() < 6) {
                password_signup.setError("Minimum length of password should be 6");
                password_signup.requestFocus();
            }

            if (email.isEmpty()) {
                email_signup.setError("Email is required");
                email_signup.requestFocus();
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                email_signup.setError("Please enter a valid email");
                email_signup.requestFocus();
            }

            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User Registered Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
