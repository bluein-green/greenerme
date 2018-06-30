package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TakeQuizPage extends AppCompatActivity {
    // view references
    private Button start_btn;
    private ImageButton home_btn;
    private TextView topic_txt;
    private TextView date_txt;

    // firebase references
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private DatabaseReference quizTopicRef;

    // topic
    private String topic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz_page);

        // determine whether user has taken quiz or not --> affects what to show


        // set firebase references
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
        quizTopicRef = dbReference.child("Quiz").child("Topic");

        // set date, topic, topic description view refs
        topic_txt = (TextView) findViewById(R.id.Quiz_topic_txt);
        date_txt = (TextView) findViewById(R.id.Quiz_date_txt);

        // get dates, topic and topic description
        quizTopicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                QuizInfo quizInfo = dataSnapshot.getValue(QuizInfo.class);

                // set quiz dates
                String date_result = getString(R.string.Quiz_startToEndDates, quizInfo.getStartDate(), quizInfo.getEndDate());
                date_txt.setText(date_result);

                // set quiz topic
                topic = quizInfo.getTopic();
                topic_txt.setText(topic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // set click listener for start button
        start_btn = (Button) findViewById(R.id.Quiz_start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakeQuizPage.this, QuizQuestionPage.class);
                intent.putExtra("TOPIC_NAME", topic);
                startActivity(intent);
            }
        });

        // set click listener for back to home button
        home_btn = (ImageButton) findViewById(R.id.Quiz_backtohome_btn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakeQuizPage.this, HomePage_loggedin.class);
                startActivity(intent);
            }
        });
    }
}
