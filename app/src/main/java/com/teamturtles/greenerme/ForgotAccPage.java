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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotAccPage extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog progressDialog;

    private EditText email_forgotAcc;
    private Button inputEmail_btn;
    private TextView logoutHomepageText_btn;
    private ImageView bigLogo;

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

        progressDialog = new ProgressDialog(this);

        email_forgotAcc = (EditText) findViewById(R.id.email_forgotAcc);
        inputEmail_btn = (Button) findViewById(R.id.inputEmail_btn);
        logoutHomepageText_btn = (TextView) findViewById(R.id.logoutHomepageText_btn);
        bigLogo = (ImageView) findViewById(R.id.bigLogo);

        inputEmail_btn.setOnClickListener(this);
        logoutHomepageText_btn.setOnClickListener(this);
        bigLogo.setOnClickListener(this);
    }

    private void resetPasswordViaEmail() {

        String email = email_forgotAcc.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()) {
            if (email.isEmpty()) {
                email_forgotAcc.setError("Email is required");
                email_forgotAcc.requestFocus();
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                email_forgotAcc.setError("Please input a valid email");
                email_forgotAcc.requestFocus();
            }
            return;
        }

        progressDialog. setMessage("Sending email...");
        progressDialog.show();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotAccPage.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent logoutHomepage_intent = new Intent(ForgotAccPage.this, HomePage_loggedout.class);
                            ForgotAccPage.this.startActivity(logoutHomepage_intent);
                        } else {
                            Toast.makeText(ForgotAccPage.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                    }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == inputEmail_btn) {
            resetPasswordViaEmail();
        }
        if (view == logoutHomepageText_btn) {
            startActivity(new Intent(this, HomePage_loggedout.class));
        }
        if (view == bigLogo) {
            startActivity(new Intent(this, HomePage_loggedout.class));
        }
    }
}
