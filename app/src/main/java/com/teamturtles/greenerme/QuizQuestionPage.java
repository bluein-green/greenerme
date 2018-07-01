package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizQuestionPage extends AppCompatActivity {
    // view references
    private TextView topic_txt;
    private TextView qnNum_txt;

    // firebase references
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private DatabaseReference quizRef;
    private DatabaseReference currQnRef;

    // question counter
    private int qnNum = 0;
    private int totalNumQn;
    private int numQnCorrect = 0;

    // questions, answers, choices
    private QuizQuestions questions;
    private QuizAnswers answers;
    private QuizChoices choices;

    // callback

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question_page);

        // get intent that started this activity
        Intent intent = getIntent();
        String topic_name = intent.getStringExtra("TOPIC_NAME");

        // set topic name
        topic_txt = (TextView) findViewById(R.id.Qn_topic);
        String topic_result = "- " + topic_name + " -";
        topic_txt.setText(topic_result);

        // set firebase references
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
        quizRef = dbReference.child("Quiz");

        // get questions from database
        readQuizData(new FirebaseCallback() {
            @Override
            public void onCallback(QuizQuestions qns) {
                System.out.println(qns.getQuestions());
                questions = qns;
                System.out.println("questions ref = " + questions);
            }
        });

        System.out.println("i am outside now");
        System.out.println(questions);


//        // get choices from database
//        quizChoicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                choices = dataSnapshot.getValue(QuizChoices.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        // get answers from database
//        quizAnswersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                answers = dataSnapshot.getValue(QuizAnswers.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        // get number of questions
//        totalNumQn = questions.getNumQuestions();
//        System.out.println("num of questions = " + totalNumQn);
//
//
//        // set question number
//        qnNum_txt = (TextView) findViewById(R.id.Qn_questionNum);


    }


    public void readQuizData(final FirebaseCallback myCallback) {
        quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questions = dataSnapshot.getValue(QuizMaterials.class);
                myCallback.onCallback(questions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private interface FirebaseCallback {
        void onCallback(QuizMaterials quizMaterials);
    }
}
