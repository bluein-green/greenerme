package com.teamturtles.greenerme;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.AuthProvider;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class LeaderboardPage extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private ArrayList<String> list;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String user_id;

    private ListView mListViewPoints;
    private ListView mListViewNames;
    private ArrayList<String> pointsAL;
    private ArrayList<String> namesAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_page);

        mListViewNames = (ListView) findViewById(R.id.names_listview);
        mListViewPoints = (ListView) findViewById(R.id.points_listview);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        pointsAL = new ArrayList<String>();
        namesAL = new ArrayList<String>();

        getScores();

        final ArrayAdapter<String> adapterNames = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, namesAL);
        final ArrayAdapter<String> adapterPoints = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, pointsAL);
        mListViewNames.setAdapter(adapterNames);
        mListViewPoints.setAdapter(adapterPoints);

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                namesAL.add(dataSnapshot.getValue(String.class));
                adapterNames.notifyDataSetChanged();
                pointsAL.add(dataSnapshot.getValue(String.class));
                adapterPoints.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                namesAL.remove(dataSnapshot.getValue(String.class));
                adapterNames.notifyDataSetChanged();
                pointsAL.remove(dataSnapshot.getValue(String.class));
                adapterPoints.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getScores() {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        user_id = currentUser.getUid();
        DatabaseReference mRef = mDatabaseReference.child("Users");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                for (DataSnapshot itemSnap : items) {
                    User user = itemSnap.getValue(User.class);
                    int points = user.getPoints();
                    String name = user.getUsername();
                    pointsAL.add(Integer.toString(points));
                    namesAL.add(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
