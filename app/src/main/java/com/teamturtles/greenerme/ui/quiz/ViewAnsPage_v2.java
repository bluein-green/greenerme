package com.teamturtles.greenerme.ui.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.model.QuizMaterials;

import java.util.ArrayList;
import java.util.List;

public class ViewAnsPage_v2 extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /**
     * Firebase references
     */
    private FirebaseDatabase database;
    private DatabaseReference quizNumQnRef;
    private DatabaseReference quizMatRef;


    private int numQns;
    private String question;
    private String answer;
    private String explanation;
    private List<QuizMaterials> quizMaterialsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ans_page_v2);

        // Set firebase references
        database = FirebaseDatabase.getInstance();
        quizNumQnRef = database.getReference().child("Quiz").child("Number of Questions");
        quizMatRef = database.getReference().child("Quiz").child("Material");

        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), ViewAnsPage_v2.this);

        // Pull number of questions from firebase
        quizNumQnRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numQns = dataSnapshot.getValue(Integer.class);

                mSectionsPagerAdapter.setData(numQns);
                mSectionsPagerAdapter.notifyDataSetChanged();
                mViewPager.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        // Pull quiz material from firebase
        readQuizData(new ViewAnsPage_v2.FirebaseCallback() {
            @Override
            public void onCallback(Iterable<DataSnapshot> dataSnapshots) {
                quizMaterialsList = new ArrayList<>();

                for (DataSnapshot matSnap : dataSnapshots) {
                    QuizMaterials quizMat = matSnap.getValue(QuizMaterials.class);
                    quizMaterialsList.add(quizMat);
                }

            }
        });






        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    public void readQuizData(final ViewAnsPage_v2.FirebaseCallback myCallback) {
        quizMatRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    private interface FirebaseCallback {
        void onCallback(Iterable<DataSnapshot> dataSnapshots);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";



        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view_ans_page_v2, container, false);
            // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            // TODO: set all the texts here!
            return rootView;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private int numQns;
        private Context context;

        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // return PlaceholderFragment.newInstance(position + 1);
            return new PlaceholderFragment(quizMaterialsList.get(position), context);
        }

        @Override
        public int getCount() {
            // Show number of pages equiv to number of questions
            return numQns;
        }

        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

        public void setData(int numQns) {
            this.numQns = numQns;
        }
    }
}
