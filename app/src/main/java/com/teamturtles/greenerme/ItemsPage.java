package com.teamturtles.greenerme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ItemsPage extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_page);

        // initialise database ref
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("Categories");
    }


}
