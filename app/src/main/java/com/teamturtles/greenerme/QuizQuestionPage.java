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
    private TextView qn_txt;

    // firebase references
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private DatabaseReference quizRef;
    private DatabaseReference currQnRef;
    private DatabaseReference numQnRef;

    // question counter
    private int qnNum = 1;
    private int totalNumQn;
    private int numQnCorrect = 0;

    // questions, answers, choices
    private String currQn;
    private int currAns;
    private ArrayList<String> currChoices;


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
        numQnRef = dbReference.child("Quiz").child("Number of Questions");

        // get number of questions from database
        numQnRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalNumQn = dataSnapshot.getValue(Integer.class);
                System.out.println("num of qn = " + totalNumQn);
                qnNum_txt = (TextView) findViewById(R.id.Qn_questionNum);
                // qnNum_txt.setText(Integer)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // get questions from database
        readQuizData(qnNum, new FirebaseCallback() {
            @Override
            public void onCallback(QuizMaterials quizMaterials) {
                currQn = quizMaterials.getQuestion();
                qn_txt = (TextView) findViewById(R.id.Qn_question);
                qn_txt.setText(currQn);

                System.out.println(quizMaterials.getQuestion());
                System.out.println(quizMaterials.getChoices());
                System.out.println(quizMaterials.getAnswer());


            }
        });


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

//
//
//        // set question number
//        qnNum_txt = (TextView) findViewById(R.id.Qn_questionNum);


    }


    public void readQuizData(int qnNum, final FirebaseCallback myCallback) {
        String query_result = "Q" + Integer.toString(qnNum);

        quizRef.child(query_result).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                QuizMaterials mat = dataSnapshot.getValue(QuizMaterials.class);
                myCallback.onCallback(mat);
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
