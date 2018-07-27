package com.teamturtles.greenerme.ui.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teamturtles.greenerme.ui.main.HomePage_loggedin;
import com.teamturtles.greenerme.R;

public class EndQuizPage extends AppCompatActivity {
    // points per answer correct
    private final int POINTS_PER_QN = 10;

    // view references
    private TextView qnsCorrect_txt;
    private TextView score_txt;
    private ImageButton home_btn;
    private Button viewAns_btn;

    // topic name
    private String topic_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_quiz_page);

        // get intent that started this activity
        Intent intent = getIntent();
        int numQnCorrect = intent.getIntExtra("NUM_QN_CORRECT", 0);
        int totalNumQn = intent.getIntExtra("TOTAL_NUM_QN", 0);
        topic_name = intent.getStringExtra("TOPIC_NAME");

        // set view refs
        setViewRefs();

        // display number of qns correct text
        displayNumQnCorrect(numQnCorrect, totalNumQn);

        // display points earned (inclusive of participation)
        displayPointsEarned(numQnCorrect);

        // set click listener for back to home
        setBackToHomeClickListener();

        // set view answers click listener
        setViewAnsClickListener();
    }


    private void setViewRefs() {
        qnsCorrect_txt = (TextView) findViewById(R.id.EQ_score_txt);
        score_txt = (TextView) findViewById(R.id.EQ_points_txt);
        home_btn = (ImageButton) findViewById(R.id.EQ_backtohome_btn);
        viewAns_btn = (Button) findViewById(R.id.EQ_viewans_btn);
    }

    private void displayNumQnCorrect(int numQnCorrect, int totalNumQn) {
        String score_res = Integer.toString(numQnCorrect) + "/" + Integer.toString(totalNumQn);
        qnsCorrect_txt.setText(score_res);
    }

    private void displayPointsEarned(int numQnCorrect) {
        int totalScore = (numQnCorrect + 1) * POINTS_PER_QN;
        String score_res = totalScore + getString(R.string.EQ_pointsearned);
        score_txt.setText(score_res);
    }

    private void setBackToHomeClickListener() {
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndQuizPage.this, HomePage_loggedin.class);
                startActivity(intent);
            }
        });
    }

    private void setViewAnsClickListener() {
        viewAns_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndQuizPage.this, ViewAnsPage_v2.class);
                intent.putExtra("TOPIC_NAME", topic_name);
                startActivity(intent);
            }
        });
    }


}
