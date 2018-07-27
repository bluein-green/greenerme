package com.teamturtles.greenerme.ui.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin;
import com.teamturtles.greenerme.ui.main.HomePage_loggedout;

public class CreateAccPage extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private EditText username_signup, password_signup, email_signup;
    private Button signup_btn;
    private TextView loginText_btn;
    private ImageView bigLogo;
    private ImageButton info_btn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc_page);


        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

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
        bigLogo = (ImageView) findViewById(R.id.bigLogo);

        info_btn = (ImageButton) findViewById(R.id.info_btn);

        info_btn.setOnClickListener(this);
        signup_btn.setOnClickListener(this);
        loginText_btn.setOnClickListener(this);
        bigLogo.setOnClickListener(this);
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
                    saveUserInfo();
                    sendVerificationEmail();
                    Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(CreateAccPage.this, Registration unsuccessful, please try again", Toast.LENGTH_SHORT).show();
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void saveUserInfo() {

        final String username = username_signup.getText().toString().trim();
        FirebaseUser user = mAuth.getCurrentUser();

        final DatabaseReference mRef = mDatabaseReference.child("Leaderboard");

        progressDialog.setMessage("Updating Username...");
        progressDialog.show();

        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    String user_id = mAuth.getCurrentUser().getUid();
                    mRef.child(user_id).child("name").setValue(username);
                    mRef.child(user_id).child("points").setValue(0);
                    mRef.child(user_id).child("hasTakenQuiz").setValue(false);

                }
            }
        });
    }

    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mAuth.signOut();
                    finish();
                    startActivity(new Intent(CreateAccPage.this, HomePage_loggedout.class));
                } else { // no idea what may this case be!!!
                    Toast.makeText(getApplicationContext(), "Please sign up again", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    //restart this activity
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == signup_btn) {
            registerUser();
        }
        if (view == loginText_btn) {
            startActivity(new Intent(this, LoginPage.class));
        }
        if (view == bigLogo) {
            startActivity(new Intent(this, HomePage_loggedout.class));
        }
        if (view == info_btn) {
            Toast.makeText(getApplicationContext(), "Password must be at least 6 characters (Blankspace not included)", Toast.LENGTH_SHORT).show();
        }
    }
}
