package com.teamturtles.greenerme.ui.quiz;

import android.content.Context;
import android.content.Intent;
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

import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.model.QuizMaterials;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin_v2;

import org.w3c.dom.Text;

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

    private TextView topic_txt;
    private ImageButton home_btn;

    /**
     * Firebase references
     */
    private FirebaseDatabase database;
    private DatabaseReference quizNumQnRef;
    private DatabaseReference quizMatRef;

    // data to be displayed
    private int numQns;
    private String question;
    private String answer;
    private String explanation;
    private List<QuizMaterials> quizMaterialsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ans_page_v2);

        // get intent that started this activity
        Intent intent = getIntent();
        String topic_name = intent.getStringExtra("TOPIC_NAME");
        System.out.println("topic name is = " + topic_name);

        topic_txt = (TextView) findViewById(R.id.Quiz_topic);
        setTopicName(topic_name);

        // Set firebase references
        database = FirebaseDatabase.getInstance();
        quizNumQnRef = database.getReference().child("Quiz").child("Number of Questions");
        quizMatRef = database.getReference().child("Quiz").child("Material");

        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


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

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Pull quiz material from firebase
        readQuizData(new ViewAnsPage_v2.FirebaseCallback() {
            @Override
            public void onCallback(Iterable<DataSnapshot> dataSnapshots) {
                quizMaterialsList = new ArrayList<>();

                for (DataSnapshot matSnap : dataSnapshots) {
                    QuizMaterials quizMat = matSnap.getValue(QuizMaterials.class);
                    quizMaterialsList.add(quizMat);
                }

                mSectionsPagerAdapter.setQuizData(quizMaterialsList);
                mSectionsPagerAdapter.notifyDataSetChanged();
                mViewPager.invalidate();
            }
        });

        home_btn = (ImageButton) findViewById(R.id.Quiz_backtohome_btn);
        setBackToHomeClickListener();



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    private void setTopicName(String topic_name) {
        String topic_result = "- " + topic_name + " -";
        topic_txt.setText(topic_result);
    }

    private void setBackToHomeClickListener() {
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAnsPage_v2.this, HomePage_loggedin_v2.class);
                startActivity(intent);
            }
        });
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
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private int numQns;
        // private Context context;
        private List<QuizMaterials> quizMaterialsList = null;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment;
            if (quizMaterialsList == null) {
                fragment = itemNotLoadedGetItem(position);
            } else {
                fragment = itemLoadedGetItem(position);
            }

            return fragment;
        }

        private Fragment itemNotLoadedGetItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        private Fragment itemLoadedGetItem(int position) {
            return PlaceholderFragment.newInstance(position + 1, quizMaterialsList.get(position));
        }

        @Override
        public int getCount() {
            // Show number of pages equiv to number of questions
            return numQns;
        }

        @Override
        public int getItemPosition(Object item) {
            System.out.println("getting item position... " + item);
            return POSITION_NONE;
        }

        public void setData(int numQns) {
            this.numQns = numQns;
        }

        public void setQuizData(List<QuizMaterials> quizMaterialsList) {
            System.out.println("set the quiz data");
            this.quizMaterialsList = quizMaterialsList;
            System.out.println(quizMaterialsList);
        }
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
        private static QuizMaterials currMat;

        /**
         * Default constrcutor - do not edit. use newInstance(.) instead
         */
        public PlaceholderFragment() {}

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

        public static PlaceholderFragment newInstance(int sectionNumber, QuizMaterials material) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();

            currMat = material;

            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("QUESTION_TEXT", currMat.getQuestion());
            args.putString("ANSWER_TEXT", currMat.getAnswerText());
            args.putString("EXP_TEXT", currMat.getExplanation());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view_ans_page_v2, container, false);

            return rootView;
        }

        /**
         * This event is triggered soon after onCreateView().
         * onViewCreated() is only called if the view returned from onCreateView() is non-null.
         * Any view setup should occur here.  E.g., view lookups and attaching view listeners.
         * @param view
         * @param savedInstanceState
         */
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
//            TextView textView = (TextView) view.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            // TODO: set all the texts here!
            // set the question num
            TextView qnNum_txt = (TextView) view.findViewById(R.id.Qn_questionNum);
            qnNum_txt.setText(getString(R.string.Qn_questionNum, sectionNum));

            // set the question text
            TextView qn_txt = (TextView) view.findViewById(R.id.Qn_question);
            qn_txt.setText(getArguments().getString("QUESTION_TEXT"));

            // set the question correct answer
            TextView ans_txt = (TextView) view.findViewById(R.id.VA_answer);
            ans_txt.setText(getArguments().getString("ANSWER_TEXT"));

            // set the explanation
            TextView exp_txt = (TextView) view.findViewById(R.id.VA_exp_txt);
            exp_txt.setText(getArguments().getString("EXP_TEXT"));


        }

    }
}
