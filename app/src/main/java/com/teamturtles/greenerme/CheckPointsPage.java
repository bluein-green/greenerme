package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckPointsPage extends AppCompatActivity implements View.OnClickListener{

    private TextView points_textView;
    private Button leaderboard_btn;
    private ImageButton home_btn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_points_page);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage_loggedout.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();
        String user_id = user.getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        points_textView = (TextView) findViewById(R.id.points_textView);
        leaderboard_btn = (Button) findViewById(R.id.leaderboard_btn);
        home_btn = (ImageButton) findViewById(R.id.Det_backtohome_btn);

        leaderboard_btn.setOnClickListener(this);
        home_btn.setOnClickListener(this);

        /*

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User loggedin_User = dataSnapshot.getValue(User.class);
                    points_textView.setText(Integer.toString(loggedin_User.getPoints()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // Don't ignore errors
                // Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }


    @Override
    public void onClick(View view) {

        if (view == leaderboard_btn) {
            Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(this, LeaderboardPage.class));
        }
        if (view == home_btn) {
            startActivity(new Intent(this, HomePage_loggedin.class));
        }

    }
}
