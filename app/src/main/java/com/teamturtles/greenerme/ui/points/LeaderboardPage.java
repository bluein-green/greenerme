package com.teamturtles.greenerme.ui.points;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin_v2;

import java.util.ArrayList;

public class LeaderboardPage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private DatabaseReference mDatabaseReference;
    private ImageButton home_btn;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String user_id;

    private ListView mListViewRanking;
    private ListView mListViewNames;
    private ListView mListViewPoints;
    private ArrayList<String> rankAL;
    private ArrayList<String> namesAL;
    private ArrayList<String> pointsAL;

    private TextView rank_LB;
    private TextView name_LB;
    private TextView points_LB;

    private TextView txt_leaderboard;

    private Button backToPts_btn;

    View clickSource;
    View touchSource;

    int offset = 0;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_page);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        home_btn = (ImageButton) findViewById(R.id.Det_backtohome_btn);
        home_btn.setOnClickListener(this);
        home_btn.setVisibility(View.INVISIBLE);

        txt_leaderboard = (TextView) findViewById(R.id.txt_leaderboard);
        txt_leaderboard.setVisibility(View.INVISIBLE);

        mListViewRanking = (ListView) findViewById(R.id.ranking_listview);
        mListViewNames = (ListView) findViewById(R.id.names_listview);
        mListViewPoints = (ListView) findViewById(R.id.points_listview);

        rank_LB = (TextView) findViewById(R.id.rank_LB);
        name_LB = (TextView) findViewById(R.id.name_LB);
        points_LB = (TextView) findViewById(R.id.points_LB);

        rank_LB.setVisibility(View.INVISIBLE);
        name_LB.setVisibility(View.INVISIBLE);
        points_LB.setVisibility(View.INVISIBLE);

        backToPts_btn = (Button) findViewById(R.id.backToPts_btn);
        backToPts_btn.setVisibility(View.INVISIBLE);
        backToPts_btn.setOnClickListener(this);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        rankAL = new ArrayList<String>();
        pointsAL = new ArrayList<String>();
        namesAL = new ArrayList<String>();

        // rank
        mListViewRanking.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchSource == null)
                    touchSource = v;

                if (v == touchSource) {
                    mListViewPoints.dispatchTouchEvent(event);
                    mListViewNames.dispatchTouchEvent(event);

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }

                return false;
            }
        });

        mListViewRanking.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view == clickSource) {
                    mListViewPoints.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                    mListViewNames.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });

        // names
        mListViewNames.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchSource == null)
                    touchSource = v;

                if (v == touchSource) {
                    mListViewPoints.dispatchTouchEvent(event);
                    mListViewRanking.dispatchTouchEvent(event);

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }

                return false;
            }
        });

        mListViewNames.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view == clickSource) {
                    mListViewPoints.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                    mListViewRanking.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });

        // points
        mListViewPoints.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchSource == null)
                    touchSource = v;

                if (v == touchSource) {
                    mListViewNames.dispatchTouchEvent(event);
                    mListViewRanking.dispatchTouchEvent(event);

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }

                return false;
            }
        });

        mListViewPoints.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view == clickSource) {
                    mListViewNames.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                    mListViewRanking.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });


        getScores();
    }


    @Override
    public void onRefresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    private void getScores() {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        user_id = currentUser.getUid();
        DatabaseReference mRef = mDatabaseReference.child("Leaderboard");

        mRef.orderByChild("points").limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 1;
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();
                final ArrayAdapter<String> adapterNames = new ArrayAdapter<String>(LeaderboardPage.this, R.layout.mylist_2, namesAL);
                final ArrayAdapter<String> adapterPoints = new ArrayAdapter<String>(LeaderboardPage.this, R.layout.mylist, pointsAL);
                final ArrayAdapter<String> adapterRanking = new ArrayAdapter<String>(LeaderboardPage.this, R.layout.mylist, rankAL);
                boolean inTop10 = false;

                for (DataSnapshot itemSnap : items) {
                    int points = itemSnap.child("points").getValue(Integer.class) * -1;
                    String name = itemSnap.child("name").getValue(String.class);
                    rankAL.add(Integer.toString(count));
                    pointsAL.add(Integer.toString(points));
                    namesAL.add(name);
                    count++;

                    if (name.equals(currentUser.getDisplayName()) && itemSnap.getKey().equals(user_id)) {
                        inTop10 = true;
                    }
                }
                if (inTop10) {
                    Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Congrats! You are in the top 10!" , Snackbar.LENGTH_INDEFINITE).show();
                } else {
                    Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Sorry! You are not in the top 10!", Snackbar.LENGTH_INDEFINITE).show();
                }

                rank_LB.setVisibility(View.VISIBLE);
                name_LB.setVisibility(View.VISIBLE);
                points_LB.setVisibility(View.VISIBLE);
                txt_leaderboard.setVisibility(View.VISIBLE);
                backToPts_btn.setVisibility(View.VISIBLE);
                home_btn.setVisibility(View.VISIBLE);
                mListViewRanking.setAdapter(adapterRanking);
                mListViewNames.setAdapter(adapterNames);
                mListViewPoints.setAdapter(adapterPoints);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something's wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == backToPts_btn) {
            // startActivity(new Intent(this, CheckPointsPage.class));
            // finish();
            onBackPressed();
        }
        if (view == home_btn) {
            startActivity(new Intent(this, HomePage_loggedin_v2.class));
        }
    }
}
