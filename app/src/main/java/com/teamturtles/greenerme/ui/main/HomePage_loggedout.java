package com.teamturtles.greenerme.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.ui.account.CreateAccPage;
import com.teamturtles.greenerme.ui.account.LoginPage;

public class HomePage_loggedout extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private static final int RC_SIGN_IN = 123;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabaseReference;

    private Button login_btn;
    private TextView signupText_btn;
    private ImageView bigLogo;

    private ImageView speech_box;
    private ImageButton email_btn;
    private ImageButton google_btn;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_loggedout);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage_loggedin_v2.class));
        }


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(HomePage_loggedout.this, HomePage_loggedin_v2.class));
                }

            }
        };

        // Configure Google Signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(HomePage_loggedout.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        mGoogleApiClient.connect();

        progressDialog = new ProgressDialog(this);

        speech_box = (ImageView) findViewById(R.id.speechbox);
        email_btn = (ImageButton) findViewById(R.id.emailButton);
        google_btn = (ImageButton) findViewById(R.id.googleButton);

        speech_box.setVisibility(View.GONE);
        email_btn.setVisibility(View.GONE);
        google_btn.setVisibility(View.GONE);

        email_btn.setOnClickListener(this);
        google_btn.setOnClickListener(this);

        login_btn = (Button) findViewById(R.id.login_btn);
        signupText_btn = (TextView) findViewById(R.id.signupText_btn);
        bigLogo = (ImageView) findViewById(R.id.bigLogo);

        login_btn.setOnClickListener(this);
        signupText_btn.setOnClickListener(this);

    }

    private void userGoogleLogin() {

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
                    Toast.makeText(HomePage_loggedout.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomePage_loggedout.this, "Authentication Successful.", Toast.LENGTH_SHORT).show();
                    checkForUser();
                }
            }
        });
    }

    public void saveUserInfo() {

        FirebaseUser user = mAuth.getCurrentUser();

        DatabaseReference mRef = mDatabaseReference.child("Leaderboard");

        String user_id = user.getUid();
        String username = user.getDisplayName();
        mRef.child(user_id).child("name").setValue(username);
        mRef.child(user_id).child("points").setValue(0);
        mRef.child(user_id).child("hasTakenQuiz").setValue(false);
    }

    private void checkForUser() {

        FirebaseUser user = mAuth.getCurrentUser();
        final String user_id = user.getUid();
        DatabaseReference mRef = mDatabaseReference.child("Leaderboard");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(user_id).exists()) {
                    saveUserInfo();
                }
                startActivity((new Intent(HomePage_loggedout.this, HomePage_loggedin_v2.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mAuth.signOut();
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == login_btn) {
            if (signupText_btn.getVisibility() == View.VISIBLE) {
                signupText_btn.setVisibility(View.GONE);
                speech_box.setVisibility(View.VISIBLE);
                email_btn.setVisibility(View.VISIBLE);
                google_btn.setVisibility(View.VISIBLE);
            } else {
                signupText_btn.setVisibility(View.VISIBLE);
                speech_box.setVisibility(View.GONE);
                email_btn.setVisibility(View.GONE);
                google_btn.setVisibility(View.GONE);
            }
        }
        if (view == email_btn) {
            startActivity(new Intent(this, LoginPage.class));
        }
        if (view == google_btn) {
            userGoogleLogin();
        }
        if (view == signupText_btn) {
            startActivity(new Intent(this, CreateAccPage.class));
        }
        if (view == bigLogo) {
            startActivity(new Intent(this, HomePage_loggedin_v2.class));
        }
    }

    @Override
    public void onBackPressed() {
        if (signupText_btn.getVisibility() == View.GONE) {
            signupText_btn.setVisibility(View.VISIBLE);
            speech_box.setVisibility(View.GONE);
            email_btn.setVisibility(View.GONE);
            google_btn.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }

    }
}
