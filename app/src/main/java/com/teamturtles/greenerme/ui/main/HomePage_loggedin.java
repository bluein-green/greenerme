package com.teamturtles.greenerme.ui.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.ui.account.ViewAccountPage;
import com.teamturtles.greenerme.ui.findItem.FindItemsPage;
import com.teamturtles.greenerme.ui.points.CheckPointsPage;
import com.teamturtles.greenerme.ui.quiz.TakeQuizPage;

public class HomePage_loggedin extends AppCompatActivity {
    private ImageButton item_btn;
    private TextView item_txt;
    private ImageButton quiz_btn;
    private TextView quiz_txt;
    private ImageButton points_btn;
    private TextView points_txt;
    private ImageButton acct_btn;
    private TextView acct_txt;
    private ImageButton about_btn;
    private TextView about_txt;

    private Button resendEmail_btn;
    private Button logoutEmail_btn;
    private TextView emailAddress;
    private Button verified_btn;

    private TextView textView;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String username;

    private AlertDialog.Builder mBuilder;
    private View mView;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_loggedin);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        loadUserInformation();

        // vary the greeting name
        //String greeting_result = getString(R.string.hi_greeting, username);
        //TextView textView = (TextView) findViewById(R.id.Hi_name);
        //textView.setText(greeting_result);


        // set view references
        setViewRefs();

        // set clicks listeners
        setClickListeners();

    }

    private void setViewRefs() {
        // SEARCH
        item_btn = (ImageButton) findViewById(R.id.search_button);
        item_txt = (TextView) findViewById(R.id.find_item_text);

        // QUIZ
        quiz_btn = (ImageButton) findViewById(R.id.quiz_button);
        quiz_txt = (TextView) findViewById(R.id.take_quiz_text);

        // POINTS
        points_btn = (ImageButton) findViewById(R.id.points_button);
        points_txt = (TextView) findViewById(R.id.check_points_text);

        // ACCOUNT SETTINGS
        acct_btn = (ImageButton) findViewById(R.id.settings_button);
        acct_txt = (TextView) findViewById(R.id.view_account_text);

        // ABOUT APP
        about_btn = (ImageButton) findViewById(R.id.about_button);
        about_txt = (TextView) findViewById(R.id.about_app_text);
    }

    private void setClickListeners() {
        // SEARCH
        item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchPage();
            }
        });
        item_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchPage();
            }
        });

        // QUIZ
        quiz_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuizPage();
            }
        });
        quiz_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuizPage();
            }
        });

        // POINTS
        points_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPointsPage();
            }
        });
        points_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPointsPage();
            }
        });

        // ACCOUNT SETTINGS
        acct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAcctPage();
            }
        });
        acct_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAcctPage();
            }
        });

        // ABOUT APP
        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutPage();
            }
        });
        about_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutPage();
            }
        });
    }

    private void openSearchPage() {
        Intent intent = new Intent(this, FindItemsPage.class);
        startActivity(intent);
    }

    private void openQuizPage() {
        Intent intent = new Intent(this, TakeQuizPage.class);
        startActivity(intent);
    }

    private void openPointsPage() {
        Intent intent = new Intent(this, CheckPointsPage.class);
        startActivity(intent);
    }

    private void openAcctPage() {
        Intent intent = new Intent(this, ViewAccountPage.class);
        startActivity(intent);
    }

    private void openAboutPage() {
        Intent intent = new Intent(this, AboutAppPage.class);
        startActivity(intent);
    }


    private void loadUserInformation() { // added

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage_loggedout.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();
        textView = (TextView) findViewById(R.id.Hi_name);

        username = user.getDisplayName();
        String greeting_result = getString(R.string.hi_greeting, username);
        textView.setText(greeting_result);

        /*
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        textView = (TextView) findViewById(R.id.Hi_name);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User loggedin_User = dataSnapshot.getValue(User.class);
                    username = loggedin_User.getUsername();
                    String greeting_result = getString(R.string.hi_greeting, username);
                    textView.setText(greeting_result);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // Don't ignore errors
                // Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });

        */

        if (!user.isEmailVerified()) {
            Toast.makeText(HomePage_loggedin.this, "Email is not verified!", Toast.LENGTH_SHORT).show();
            emailVerifiedPop();
        }

    }


    private void emailVerifiedPop() {

        mBuilder = new AlertDialog.Builder(this);
        mView = getLayoutInflater().inflate(R.layout.dialog_email_not_verified_pop, null);

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();

        resendEmail_btn = (Button) mView.findViewById(R.id.resendEmail_btn);
        logoutEmail_btn = (Button) mView.findViewById(R.id.logoutEmail_btn);
        verified_btn = (Button) mView.findViewById(R.id.verified_btn);

        resendEmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendEmail();
            }
        });
        logoutEmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mAuth.signOut();
                startActivity(new Intent(HomePage_loggedin.this, HomePage_loggedout.class));
            }
        });

        verified_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.reload();

                if (!user.isEmailVerified()) {
                    Toast.makeText(HomePage_loggedin.this, "Email is not verified! Please try again! ", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                }
            }
        });

    }
    private void resendEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Successfully Resend Email", Toast.LENGTH_SHORT).show();
                } else { // no idea what may this case be!!!
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
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
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            dialog.dismiss();
            finish();
            return true;
        }
        return false;
    }
}