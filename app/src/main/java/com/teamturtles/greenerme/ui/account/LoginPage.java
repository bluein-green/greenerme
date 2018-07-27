package com.teamturtles.greenerme.ui.account;

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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin_v2;
import com.teamturtles.greenerme.ui.main.HomePage_loggedout;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;

    private EditText email_login, password_login;
    private Button signin_btn;
    private Button signinGoogle_btn;
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

        /*
        private FirebaseAuth.AuthStateListener mAuthListener;
        mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(getApplicationContext(), HomePage_loggedin.class));
            }
        }
        */

        progressDialog = new ProgressDialog(this);

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);

        signin_btn = (Button) findViewById(R.id.signin_btn);
      //  signinGoogle_btn = (Button) findViewById(R.id.signinGoogle_btn);
        forgotAccText_btn = (TextView) findViewById(R.id.forgotAccText_btn);
        bigLogo = (ImageView) findViewById(R.id.bigLogo);

        signin_btn.setOnClickListener(this);
//        signinGoogle_btn.setOnClickListener(this);
        forgotAccText_btn.setOnClickListener(this);
        bigLogo.setOnClickListener(this);
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }
    */

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
                    startActivity(new Intent(getApplicationContext(), HomePage_loggedin.class));
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void userLoginGoogle() {

        // Configure Google Signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(LoginPage.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        progressDialog.setMessage("Logging in User with Google Account...");
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();

                if (!task.isSuccessful()) {
                    Toast.makeText(LoginPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginPage.this, "Authentication Successful.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == signin_btn) {
            userLogin();
        }
        if (view == signinGoogle_btn) {
            userLoginGoogle();
        }
        if (view == forgotAccText_btn) {
            finish();
            startActivity(new Intent(this, ForgotAccPage.class));
        }
        if (view == bigLogo) {
            startActivity(new Intent(this, HomePage_loggedout.class));
        }
    }
}