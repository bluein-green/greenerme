package com.teamturtles.greenerme;

import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Ref;

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

    private TextView textView; // changed
    private FirebaseAuth mAuth; // changed

    private String username; // changed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_loggedin);

        loadUserInformation(); // changed

        // vary the greeting name
        //String greeting_result = getString(R.string.hi_greeting, username);
        //TextView textView = (TextView) findViewById(R.id.Hi_name);
        //textView.setText(greeting_result);


        // pop-up text
        Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();

        // click listeners for SEARCH
        item_btn = (ImageButton) findViewById(R.id.search_button);
        item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchPage();
            }
        });

        item_txt = (TextView) findViewById(R.id.find_item_text);
        item_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchPage();
            }
        });

        // click listeners for QUIZ
        quiz_btn = (ImageButton) findViewById(R.id.quiz_button);
        quiz_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuizPage();
            }
        });

        quiz_txt = (TextView) findViewById(R.id.take_quiz_text);
        quiz_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuizPage();
            }
        });

        // click listeners for POINTS
        points_btn = (ImageButton) findViewById(R.id.points_button);
        points_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPointsPage();
            }
        });

        points_txt = (TextView) findViewById(R.id.check_points_text);
        points_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPointsPage();
            }
        });

        // click listeners for ACCOUNT
        acct_btn = (ImageButton) findViewById(R.id.settings_button);
        acct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAcctPage();
            }
        });

        acct_txt = (TextView) findViewById(R.id.view_account_text);
        acct_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAcctPage();
            }
        });

        // click listeners for ABOUT APP
        about_btn = (ImageButton) findViewById(R.id.about_button);
        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutPage();
            }
        });

        about_txt = (TextView) findViewById(R.id.about_app_text);
        about_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutPage();
            }
        });
    }

    public void openSearchPage() {
        Intent intent = new Intent(this, FindItemsPage.class);
        startActivity(intent);
    }

    public void openQuizPage() {
        Intent intent = new Intent(this, TakeQuizPage.class);
        startActivity(intent);
    }

    public void openPointsPage() {
        Intent intent = new Intent(this, CheckPointsPage.class);
        startActivity(intent);
    }

    public void openAcctPage() {
        Intent intent = new Intent(this, ViewAccountPage.class);
        startActivity(intent);
    }

    public void openAboutPage() {
        Intent intent = new Intent(this, AboutAppPage.class);
        startActivity(intent);
    }


    private void loadUserInformation() { // added

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage_loggedout.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();
        String user_id = user.getUid();

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
    }

    public void logOut(View view) {
        finish();
        mAuth.signOut();
        startActivity(new Intent(this, HomePage_loggedout.class));
    }
}
