package com.teamturtles.greenerme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateAccPage extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private EditText username_signup, password_signup, email_signup;
    private Button signup_btn;
    private TextView loginText_btn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc_page);


        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage_loggedin.class));
        }

        progressDialog = new ProgressDialog(this);

        username_signup = (EditText) findViewById(R.id.username_signup);
        password_signup = (EditText) findViewById(R.id.password_signup);
        email_signup = (EditText) findViewById(R.id.email_signup);

        signup_btn = (Button) findViewById(R.id.signup_btn);
        loginText_btn = (TextView) findViewById(R.id.loginText_btn);

        signup_btn.setOnClickListener(this);
        loginText_btn.setOnClickListener(this);
    }

    private void registerUser() {

        String username = username_signup.getText().toString().trim();
        String password = password_signup.getText().toString().trim();
        String email = email_signup.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || password.length() < 6 || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (username.isEmpty()) {
                username_signup.setError("Username is required");
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

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Registered Successfully", Toast.LENGTH_SHORT).show();
                    saveUserInfo();
                } else {
                    // Toast.makeText(CreateAccPage.this, Registration unsuccessful, please try again", Toast.LENGTH_SHORT).show();
                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(),"You are already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void saveUserInfo() {

        String username = username_signup.getText().toString().trim();
        String email = email_signup.getText().toString().trim();
        User newUser = new User(username, email);

        String user_id = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).setValue(newUser);
        mAuth.signOut();

        finish();
        Intent logoutHomepage_intent = new Intent(CreateAccPage.this, HomePage_loggedout.class);
        CreateAccPage.this.startActivity(logoutHomepage_intent);
     }

     /*
     public void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Re-enable button
                findViewById(R.id.verify_email_button.setEnabled(true);

                if (task.isSuccessful()) {
                    Toast.makeText(EmailPasswordActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(EmailPasswordActivitythis, "Failed to send verification email.", Toast.LENGTH_SHORT()).show();
                }
            }
        })
     }
     */

    @Override
    public void onClick(View view) {
        if(view == signup_btn) {
            registerUser();
        }
        if (view == loginText_btn) {
            startActivity(new Intent(this, LoginPage.class));
        }
    }
}
