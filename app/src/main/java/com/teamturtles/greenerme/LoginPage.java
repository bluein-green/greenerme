package com.teamturtles.greenerme;

import android.app.ProgressDialog;
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

public class LoginPage extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    private EditText email_login, password_login;
    private Button signin_btn;
    private TextView forgotAccText_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);


        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage_loggedin.class));
        }

        progressDialog = new ProgressDialog(this);

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);

        signin_btn = (Button) findViewById(R.id.signin_btn);
        forgotAccText_btn = (TextView) findViewById(R.id.forgotAccText_btn);

        signin_btn.setOnClickListener(this);
        forgotAccText_btn.setOnClickListener(this);
    }

    private void userLogin() {

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

        progressDialog. setMessage("Logging in User...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), HomePage_loggedin.class));
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == signin_btn) {
            userLogin();
        }
        if (view == forgotAccText_btn) {
            finish();
            startActivity(new Intent(this, ForgotAccPage.class));
        }
    }
}