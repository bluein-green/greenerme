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

public class LoginPage extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText email_login, password_login;
    Button signin_btn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth. = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.email_login);
        signin_btn = (Button) findViewById(R.id.signin_btn);

        TextView forgotAccText_btn = (TextView) findViewById(R.id.forgotAccText_btn);

        forgotAccText_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotAcc_intent = new Intent(LoginPage.this, ForgotAccPage.class);
                LoginPage.this.startActivity(forgotAcc_intent);
            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignin();
            }
        });
    }

    private void userSignin() {

        String email = email_login.getText().toString().trim();
        String password = password_login.getText().toString().trim();

        if (password.isEmpty() || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            if (password.isEmpty()) {
                password_login.setError("Password is required");
                password_login.requestFocus();
            }

            if (email.isEmpty()) {
                email_login.setError("Email is required");
                email_login.requestFocus();
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                email_login.setError("Please enter a valid email");
                email_login.requestFocus();
            }
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                ProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent signin_intent = new Intent(LoginPage.this, HomePage_loggedin.class);
                    signin_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signin_intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        })

    }
}