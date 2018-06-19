package com.teamturtles.greenerme;

import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_loggedin);

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

}
