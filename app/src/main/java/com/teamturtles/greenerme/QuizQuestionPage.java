package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionPage extends AppCompatActivity {
    // view references
    private TextView topic_txt;
    private TextView qnNum_txt;
    private TextView qn_txt;
    private List<Button> choices = new ArrayList<>();
    private Button choice1_txt;
    private Button choice2_txt;
    private Button choice3_txt;
    private Button choice4_txt;
    private Button next_btn;


    // firebase references
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private DatabaseReference quizRef;
    private DatabaseReference numQnRef;

    // question counter
    private int qnNum = 0;
    private int totalNumQn;
    private int numQnCorrect = 0;

    // questions, answers, choices
    private String currQn;
    private int currAns;
    private int numCurrChoices;
    private int clickedChoiceNum;

    // quiz materials
    private List<QuizMaterials> quizMaterialsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question_page);

        // get intent that started this activity
        Intent intent = getIntent();
        String topic_name = intent.getStringExtra("TOPIC_NAME");

        // set view references
        this.setViewRefs();


        // set topic name
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        // get quiz material from database
        readQuizData(new FirebaseCallback() {
            @Override
            public void onCallback(Iterable<DataSnapshot> dataSnapshots) {
                quizMaterialsList = new ArrayList<>();

                for (DataSnapshot matSnap : dataSnapshots) {
                    QuizMaterials quizMat = matSnap.getValue(QuizMaterials.class);
                    quizMaterialsList.add(quizMat);
                }

                // set quiz material for FIRST question
                setCurrQuizMaterial(qnNum);
            }
        });
    }

    public void readQuizData(final FirebaseCallback myCallback) {
        quizRef.child("Material").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> quizMat = dataSnapshot.getChildren();
                myCallback.onCallback(quizMat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setViewRefs() {
        // topic name
        topic_txt = (TextView) findViewById(R.id.Qn_topic);

        // set other view references
        qnNum_txt = (TextView) findViewById(R.id.Qn_questionNum);
        qn_txt = (TextView) findViewById(R.id.Qn_question);
        choice1_txt = (Button) findViewById(R.id.Qn_choice1);
        choice2_txt = (Button) findViewById(R.id.Qn_choice2);
        choice3_txt = (Button) findViewById(R.id.Qn_choice3);
        choice4_txt = (Button) findViewById(R.id.Qn_choice4);

        choices.add(choice1_txt);
        choices.add(choice2_txt);
        choices.add(choice3_txt);
        choices.add(choice4_txt);

        // set next button ref
        next_btn = (Button) findViewById(R.id.Qn_next_btn);
    }

    private void setCurrQuizMaterial(int qnNum) {
        setNumCurrChoices(qnNum);
        setQuestionOnScreen(qnNum);
        setCorrectAns(qnNum);
        // System.out.println("correct ans = " + currAns);
        setClickListeners();
    }

    private void setClickListeners() {
        for (int i = 0; i < numCurrChoices; i++) {
            final int counter = i;
            choices.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set clicked choice number to change views
                    clickedChoiceNum = counter;

                    // check if it is the correct answer, based on the choice number
                    if (checkCorrectAnswer(counter)) {  // is correct
                        // change to green colour
                        setCorrectAnsColour(counter);

                        // update number of questions gotten correct
                        updateNumQnCorrect();

                    } else {
                        // change to red colour, highlight correct ans
                        setWrongAnsColour(counter);
                        setCorrectAnsColour(getCorrectAns());
                    }

                    // grey out the other options
                    greyOutOtherOptions();

                    // remove click listeners after clicking
                    removeChoicesClickListeners();

                    // show explanation for question

                    // show NEXT button
                    next_btn.setVisibility(View.VISIBLE);
                    if (qnNum < totalNumQn - 1) {
                        setNextButtonClickListener();
                    } else { // show END QUIZ button
                        next_btn.setText(R.string.Qn_end_quiz);
                        setEndButtonClickListener();
                    }
                }
            });

        }
    }

    private void removeChoicesClickListeners() {
        for (int i = 0; i < numCurrChoices; i++) {
            choices.get(i).setOnClickListener(null);
        }
    }

    private void setNextButtonClickListener() {
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultChoicesColour();
                qnNum++;
                setCurrQuizMaterial(qnNum);
            }
        });
    }

    private void setEndButtonClickListener() {
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open End Quiz page
                Intent intent = new Intent(QuizQuestionPage.this, EndQuizPage.class);
                intent.putExtra("NUM_QN_CORRECT", (int) numQnCorrect);  // +1 for participation
                intent.putExtra("TOTAL_NUM_QN", (int) totalNumQn);

                System.out.println("correct = " + numQnCorrect);
                System.out.println("total qns = " + totalNumQn);

                startActivity(intent);
                finish();   // prevent user from going back to quiz
            }
        });
    }

    private void setDefaultChoicesColour() {
        // get all 4 options back
        for (int i = 0; i < 4; i++) {   // default max num of options = 4
            choices.get(i).setVisibility(View.VISIBLE);
            choices.get(i).setBackgroundColor(0xFF83b8d3);
        }
    }

    private void setWrongAnsColour(int choiceNum) {
        choices.get(choiceNum).setBackgroundColor(0xFFFF9580); //0xFF9580
    }

    private void setCorrectAnsColour(int choiceNum) {
        choices.get(choiceNum).setBackgroundColor(0xFF3EB489);
    }

    private void greyOutOtherOptions() {
        for (int i = 0; i < numCurrChoices; i++) {
            if (i == this.clickedChoiceNum || i == this.currAns) {
                continue;
            }

            choices.get(i).setBackgroundColor(0xFFE0E0E0);
        }
    }


    private void setQuestionOnScreen(int qnNum) {
        // set question number
        String qnNum_res = "Question " + (qnNum + 1);
        qnNum_txt.setText(qnNum_res);

        // set question text
        currQn = quizMaterialsList.get(qnNum).getQuestion();
        qn_txt.setText(currQn);

        // display only the buttons (and text) for the specified number of choices
        for (int i = 0; i < this.numCurrChoices; i++) {
            // NOTE: choice number and button number are 0-base indexed
            choices.get(i).setText(quizMaterialsList.get(qnNum).getChoice(i));
        }

        // set rest of the options (if necessary) to visibility.GONE
        for (int i = 3; i >= this.numCurrChoices; i--) {
            choices.get(i).setVisibility(View.GONE);
        }

        // set the next button to visibility.GONE
        next_btn.setVisibility(View.GONE);

    }

    private void setNumCurrChoices(int qnNum) {
        numCurrChoices = quizMaterialsList.get(qnNum).getNumChoices();
    }


    private void setCorrectAns(int qnNum) {
        this.currAns = quizMaterialsList.get(qnNum).getAnswer();
    }

    private int getCorrectAns() {
        return this.currAns;
    }

    private boolean checkCorrectAnswer(int ans) {
        if (ans == this.currAns) {
            return true;
        } else {
            return false;
        }
    }

    private void updateNumQnCorrect() {
        numQnCorrect++;
    }


    private interface FirebaseCallback {
        void onCallback(Iterable<DataSnapshot> dataSnapshots);
    }

}
