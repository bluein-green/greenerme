package com.teamturtles.greenerme;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamturtles.greenerme.model.Item;

import java.util.ArrayList;
import java.util.List;

public class temp_toremove extends AppCompatActivity {
    private EditText name;
    private EditText recyclability;
    private EditText HDB_recyclable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_toremove);

        // for fields
        name = (EditText) findViewById(R.id.editText);
        recyclability = (EditText) findViewById(R.id.editText2);
        HDB_recyclable = (EditText) findViewById(R.id.editText3);

        // set firebase references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference();
        final DatabaseReference catRef = dbReference.child("testbranch");


        // add listener for submit button
        Button submit_btn = (Button) findViewById(R.id.button);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Item item = new Item();
//
//                item.setName(name.getText().toString());
//                item.setRecyclability(Integer.parseInt(recyclability.getText().toString()));
//                item.setStatus(Boolean.parseBoolean(HDB_recyclable.getText().toString()), false, false);
//                item.setProcedure("testing only");
//                ArrayList<String> lol = new ArrayList<>();
//                lol.add("this is the first step");
//                lol.add("this is step 2");
//                lol.add("why am i doing this");
//                item.setTest(lol);

                QuizQuestions qns = new QuizQuestions();
                ArrayList<String> act = new ArrayList<>();
                act.add("lolol");
                act.add("hello from this side");
                qns.setQuestions(act);

                catRef.push().setValue(qns);
            }
        });


        // to view fetched text
        final TextView dispResult = (TextView) findViewById(R.id.textView5);
        final List<Item> items = new ArrayList<Item>();
        // final DatabaseReference itemsRef = dbReference.child("Item categories").child("Paper");

        // add listener for fetch button
        Button fetch_btn = (Button) findViewById(R.id.button2);
        fetch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                        // shake hands with each of them
                        for (DataSnapshot child : children) {
                            Item rItem = child.getValue(Item.class);
                            items.add(rItem);
                        }


                        // Item rItem = dataSnapshot.getValue(Item.class);
                        //String rItem_name = rItem.getName();


                        // dispResult.setText(rItem_name);

                        // make an ArrayAdapter to show results
                        ArrayAdapter<Item> itemArrayAdapter = new ArrayAdapter<Item>(temp_toremove.this, android.R.layout.simple_list_item_1, items);

                        ListView listView = (ListView) findViewById(R.id.list_demo);
                        listView.setAdapter(itemArrayAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }


    /*
    public void onSubmitClicked(View view) {
        // database reference


        // create an object to hold info
        Item item = new Item();

        // populate the info with values from the screen
        item.setName(name.getText().toString());
        item.setRecyclability(Integer.parseInt(recyclability.getText().toString()));
        item.setStatus(Boolean.parseBoolean(HDB_recyclable.getText().toString()), false, false);

        // push into firebase
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dbReference = database.getReference();
            DatabaseReference catRef = dbReference.child("testbranch");
            catRef.push().setValue(item);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "cannot save", Toast.LENGTH_LONG).show();
        }
    }

    */

}
