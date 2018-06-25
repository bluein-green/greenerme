package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotAccPage extends AppCompatActivity {

    private EditText email_forgotAcc;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_acc_page);

        email_forgotAcc = (EditText) findViewById(R.id.email_forgotAcc);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage_loggedin.class));
        }

        TextView logoutHomepageText_btn = (TextView) findViewById(R.id.logoutHomepageText_btn);
        logoutHomepageText_btn.setOnClickListener(new View.OnClickListener() { // To click to the createe account page
            @Override
            public void onClick(View view) { // To click to the create account page
                Intent logoutHomepage_intent = new Intent(ForgotAccPage.this, HomePage_loggedout.class);
                ForgotAccPage.this.startActivity(logoutHomepage_intent);
            }
        });
    }

    private void checkEmail() {

        String email = email_forgotAcc.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_forgotAcc.setError("Please input a valid email");
            email_forgotAcc.requestFocus();
        }

        // put a progress bar

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotAccPage.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotAccPage.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void resetPassword (View view) { // send email to reset password
        checkEmail();
    }
}
