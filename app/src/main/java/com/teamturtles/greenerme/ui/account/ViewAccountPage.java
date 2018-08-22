package com.teamturtles.greenerme.ui.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin_v2;
import com.teamturtles.greenerme.ui.main.HomePage_loggedout;

import java.util.List;

public class ViewAccountPage extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String user_id;
    private DatabaseReference databaseReference;

    private Button changeUsername_btn;
    private Button changePassword_btn;
    private Button deleteAcc_btn;
    private Button logout_btn;
    private ImageButton home_btn;

    private AlertDialog.Builder mBuilder;
    private View mView;
    private AlertDialog dialog;

    private Button cancel_btn;
    private Button editUsernamePop_btn;
    private Button changePasswordPop_btn;
    private Button deleteAccPop_btn;
    private Button deleteAccGooglePop_btn;

    private EditText oldPassword_EditText, newPassword_EditText, confirmPassword_EditText;
    private EditText newUsername_EditText;
    private EditText password_EditText;

    private TextView username_TextView;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account_page);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        user_id = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        changeUsername_btn = (Button) findViewById(R.id.changeUsername_btn);
        changePassword_btn = (Button) findViewById(R.id.changePassword_btn);
        deleteAcc_btn = (Button) findViewById(R.id.deleteAcc_btn);
        logout_btn = (Button) findViewById(R.id.logout_btn);
        home_btn = (ImageButton) findViewById(R.id.Det_backtohome_btn);

        changeUsername_btn.setOnClickListener(this);
        changePassword_btn.setOnClickListener(this);
        deleteAcc_btn.setOnClickListener(this);
        logout_btn.setOnClickListener(this);
        home_btn.setOnClickListener(this);
    }


    private void changeUsernamePop() {

        mBuilder = new AlertDialog.Builder(this);
        mView = getLayoutInflater().inflate(R.layout.dialog_edit_username_pop, null);

        editUsernamePop_btn = (Button) mView.findViewById(R.id.editUsernamePop_btn);
        cancel_btn = (Button) mView.findViewById(R.id.cancel_btn);

        newUsername_EditText = (EditText) mView.findViewById(R.id.newUsername_EditText);
        username_TextView = (TextView) mView.findViewById(R.id.Username_TextView);
        username_TextView.setText(currentUser.getDisplayName());

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();

        cancel_btn.setOnClickListener(this);
        editUsernamePop_btn.setOnClickListener(this);
    }

    private void changeUsername() {

        String new_username = newUsername_EditText.getText().toString().trim();

        if (new_username.isEmpty()) {
            newUsername_EditText.setError("Username is required");
            newUsername_EditText.requestFocus();
            return;
        }

        progressDialog.setMessage("Editing Username...");
        progressDialog.show();

        final String newUsername_final = new_username;
        final DatabaseReference usernameDatabase = databaseReference.child("Leaderboard").child(user_id).child("name");

        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(newUsername_final).build();
        currentUser.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();

                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    usernameDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                usernameDatabase.setValue(newUsername_final);

                                Toast.makeText(getApplicationContext(), "Username has been edited!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


    private void changePasswordPop() {

        mBuilder = new AlertDialog.Builder(this);
        mView = getLayoutInflater().inflate(R.layout.dialog_reset_password_pop, null);

        changePasswordPop_btn = (Button) mView.findViewById(R.id.changePasswordPop_btn);
        cancel_btn = (Button) mView.findViewById(R.id.cancel_btn);

        oldPassword_EditText = (EditText) mView.findViewById(R.id.oldPassword_EditText);
        newPassword_EditText = (EditText) mView.findViewById(R.id.newPassword_EditText);
        confirmPassword_EditText = (EditText) mView.findViewById(R.id.confirmPassword_EditText);

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();

        cancel_btn.setOnClickListener(this);
        changePasswordPop_btn.setOnClickListener(this);

    }

    private void changePassword() {

        String oldPassword = oldPassword_EditText.getText().toString().trim();
        String newPassword = newPassword_EditText.getText().toString().trim();
        String confirmPassword = confirmPassword_EditText.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || newPassword.length() < 6 || confirmPassword.isEmpty()) {
            if (oldPassword.isEmpty()) {
                oldPassword_EditText.setError("Password is required");
                oldPassword_EditText.requestFocus();
            }

            if (newPassword.isEmpty()) {
                newPassword_EditText.setError("Password is required");
                newPassword_EditText.requestFocus();
            }

            if (newPassword.length() < 6) {
                newPassword_EditText.setError("Minimum length of password should be 6");
                newPassword_EditText.requestFocus();
            }

            if (confirmPassword.isEmpty()) {
                confirmPassword_EditText.setError("Password is required");
                confirmPassword_EditText.requestFocus();
            }

            if (!newPassword.equals(confirmPassword)) {
                confirmPassword_EditText.setError("Password does not match");
                confirmPassword_EditText.requestFocus();
            }
            return;
        }

        progressDialog.setMessage("Changing Password...");
        progressDialog.show();

        final String email = currentUser.getEmail();
        final String password_final = newPassword;
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

        currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    currentUser.updatePassword(password_final).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void deleteAccPop() {

        mBuilder = new AlertDialog.Builder(this);
        mView = getLayoutInflater().inflate(R.layout.dialog_delete_acc_pop, null);

        deleteAccPop_btn = (Button) mView.findViewById(R.id.deleteAccPop_btn);
        cancel_btn = (Button) mView.findViewById(R.id.cancel_btn);

        password_EditText = (EditText) mView.findViewById(R.id.password_EditText);

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();

        cancel_btn.setOnClickListener(this);
        deleteAccPop_btn.setOnClickListener(this);
    }

    private void deleteAccGooglePop() {

        mBuilder = new AlertDialog.Builder(this);
        mView = getLayoutInflater().inflate(R.layout.dialog_delete_acc_google_pop, null);

        deleteAccGooglePop_btn = (Button) mView.findViewById(R.id.deleteAccGooglePop_btn);
        cancel_btn = (Button) mView.findViewById(R.id.cancel_btn);

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();

        cancel_btn.setOnClickListener(this);
        deleteAccGooglePop_btn.setOnClickListener(this);
    }


    private void deleteAcc() {
        try {
            final String email = currentUser.getEmail();
            String password = password_EditText.getText().toString().trim();    // <-- might be an empty string

            AuthCredential credential = EmailAuthProvider.getCredential(email, password); // <-- might have IllegalArgumentException

            progressDialog.setMessage("Authenticating Password...");
            progressDialog.show();

            currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        finish();
                        delete();

                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You did not enter your password", Toast.LENGTH_SHORT).show();
        }
    }

    private void delete() {

        progressDialog.setMessage("Deleting User...");
        progressDialog.show();

        // delete user info from database
        FirebaseDatabase.getInstance().getReference().child("Leaderboard").child(user_id).removeValue();

        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "User have been deleted!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity((new Intent(ViewAccountPage.this, HomePage_loggedin_v2.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onClick(View view) {

        if (view == changeUsername_btn) {
            changeUsernamePop();
        }
        if (view == changePassword_btn) {
            List<String> info = currentUser.getProviders();
            if (info.contains("google.com")) {
                Toast.makeText(getApplicationContext(), "Does not apply for Google Sign In!", Toast.LENGTH_SHORT).show();
            } else {
                changePasswordPop();
            }
        }
        if (view == deleteAcc_btn) {
            List<String> info = currentUser.getProviders();
            if (info.contains("google.com")) {
                deleteAccGooglePop();
            } else {
                deleteAccPop();
            }
        }
        if (view == logout_btn) {
            finish();
            mAuth.signOut();
            if (mAuth.getCurrentUser() == null) {
                finish();
                startActivity((new Intent(getApplicationContext(), HomePage_loggedin_v2.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }

        }
        if (view == cancel_btn) {
            // SetBackground();
            dialog.dismiss();
        }
        if (view == editUsernamePop_btn) {
            changeUsername();
        }
        if (view == changePasswordPop_btn) {
            changePassword();
        }
        if (view == deleteAccPop_btn) {
            deleteAcc();

        }
        if (view == deleteAccGooglePop_btn) {
            delete();
        }
        if (view == home_btn) {
            // startActivity(new Intent(this, HomePage_loggedin_v2.class));
            onBackPressed();
        }
    }
}
