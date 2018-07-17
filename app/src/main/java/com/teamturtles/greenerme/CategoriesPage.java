package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CategoriesPage extends AppCompatActivity {
    // for expandable list view
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, List<Pair>> expandableListDetail;

    // for BACK TO HOME button
    private ImageButton home_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_page);

        // set view references
        setViewRefs();

        // set click listener for BACK TO HOME
        setBackToHomeClickListener();


        // set expandable list view references and get data
        expandableListView = (ExpandableListView) findViewById(R.id.IC_expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();    // get data from that class
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
/*
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition).getName(), Toast.LENGTH_SHORT
                ).show();

                System.out.println("group pos = " + groupPosition);
                System.out.println("child pos = " + childPosition);
                System.out.println("id = " + id);
*/
                Intent intent = new Intent(CategoriesPage.this, ItemDetailsPage.class);
                intent.putExtra("ITEM_ID", (int) id);
                startActivity(intent);

                return false;
            }
        });

        /*
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });
*/
    }

    private void setViewRefs() {
        home_btn = (ImageButton) findViewById(R.id.IC_backtohome_btn);
    }


    private void setBackToHomeClickListener() {
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesPage.this, HomePage_loggedin.class);
                startActivity(intent);
            }
        });
    }

}

