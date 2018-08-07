package com.teamturtles.greenerme.ui.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin_v2;
import com.teamturtles.greenerme.ui.main.HomePage_loggedout;

import java.util.Arrays;
import java.util.List;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    private EditText email_login, password_login;
    private Button signin_btn;
    private TextView forgotAccText_btn;
    private ImageView bigLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage_loggedin_v2.class));
        }

        progressDialog = new ProgressDialog(this);

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);

        signin_btn = (Button) findViewById(R.id.signin_btn);
        forgotAccText_btn = (TextView) findViewById(R.id.forgotAccText_btn);
        bigLogo = (ImageView) findViewById(R.id.bigLogo);

        signin_btn.setOnClickListener(this);
        forgotAccText_btn.setOnClickListener(this);
        bigLogo.setOnClickListener(this);
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

        progressDialog.setMessage("Logging in User...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), HomePage_loggedin_v2.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        if (view == bigLogo) {
            startActivity(new Intent(this, HomePage_loggedin_v2.class));
        }
    }
}