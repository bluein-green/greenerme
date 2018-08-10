package com.teamturtles.greenerme.ui.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin;
import com.teamturtles.greenerme.model.QuizInfo;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin_v2;

public class TakeQuizPage extends AppCompatActivity {
    // view references
    private Button start_btn;
    private ImageButton home_btn;
    private TextView topic_txt;
    private TextView date_txt;
    private TextView description_txt;
    private TextView takenQuiz_txt;
    private TextView focus_txt;

    // firebase references
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private DatabaseReference quizTopicRef;
    private DatabaseReference hasTakenQuizRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;

    // topic
    private String topic;

    // has taken quiz
    private boolean hasTakenQuiz;

    @Override
    public void onResume() {
        super.onResume();
        pullTakenQuizInfo();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz_page);

        // set firebase authentication references
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();

        // set firebase database references
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
        quizTopicRef = dbReference.child("Quiz").child("Topic");
        hasTakenQuizRef = dbReference.child("Leaderboard").child(userId).child("hasTakenQuiz");

        // determine whether user has taken quiz or not
        // sets the buttons too
        pullTakenQuizInfo();

        // set date, topic, topic description view refs
        setViewRefs();


        // get dates, topic and topic description
        quizTopicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                QuizInfo quizInfo = dataSnapshot.getValue(QuizInfo.class);

                // set quiz dates
                String date_result = getString(R.string.Quiz_startToEndDates, quizInfo.getStartDate(), quizInfo.getEndDate());
                date_txt.setText(date_result);
                date_txt.setVisibility(View.VISIBLE);

                // set quiz topic
                topic = quizInfo.getTopic();
                topic_txt.setText(topic);

                // set quiz description
                String description = quizInfo.getDescription();
                description_txt.setText(description);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        // set click listener for back to home button
        setBackToHomeClickListener();

    }

    private void setViewRefs() {
        topic_txt = (TextView) findViewById(R.id.Quiz_topic_txt);
        date_txt = (TextView) findViewById(R.id.Quiz_date_txt);
        description_txt = (TextView) findViewById(R.id.Quiz_description_txt);
        start_btn = (Button) findViewById(R.id.Quiz_start_btn);
        home_btn = (ImageButton) findViewById(R.id.Quiz_backtohome_btn);

        // to vary depending on whether user has taken quiz
        focus_txt = (TextView) findViewById(R.id.Quiz_focus_txt);
        takenQuiz_txt = (TextView) findViewById(R.id.Quiz_taken_enc);

        date_txt.setVisibility(View.INVISIBLE);
    }

    private void pullTakenQuizInfo() {
        hasTakenQuizRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hasTakenQuiz = dataSnapshot.getValue(Boolean.class);

                System.out.println("USER TAKEN QUIZ = " + hasTakenQuiz);

                // set appropriate button
                chooseStartOrReviewBtn();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void chooseStartOrReviewBtn() {
        if (hasTakenQuiz) {
            // change to review button
            start_btn.setBackgroundResource(R.drawable.button_blue);
            start_btn.setText(R.string.Quiz_viewAns);
            start_btn.setTextColor(Color.parseColor("#ffffff"));

            // set click listener (go to review page)
            setReviewBtnClickListener();

            // change the messages
            changeHasTakenMessage();

        } else {
            // don't need to change, but set the click listener for start button
            setStartBtnClickListener();
        }
    }

    private void setStartBtnClickListener() {
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakeQuizPage.this, QuizQuestionPage.class);
                intent.putExtra("TOPIC_NAME", topic);
                startActivity(intent);
                hasTakenQuizRef.setValue(true);
            }
        });
    }

    private void setReviewBtnClickListener() {
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TakeQuizPage.this, ViewAnsPage_v2.class);
                intent.putExtra("TOPIC_NAME", topic);
                startActivity(intent);
            }
        });
    }

    private void changeHasTakenMessage() {
        takenQuiz_txt.setVisibility(View.VISIBLE);
        focus_txt.setText(R.string.Quiz_triedAlr);
    }

    private void setBackToHomeClickListener() {
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(TakeQuizPage.this, HomePage_loggedin_v2.class);
                // startActivity(intent);
                onBackPressed();
            }
        });
    }
}
