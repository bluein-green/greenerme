package com.teamturtles.greenerme;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemDetailsPage extends AppCompatActivity {
    // displays
    private ImageButton home_btn;
    private TextView itemName_txt;
    private ImageView recyclable_icon;
    private TextView recyclable_txt;
    private TextView howto_txt;
    private ImageView hdb_icon;
    private ImageView sep_icon;

    // firebase references
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private DatabaseReference itemRef;

    // item instance
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_page);

        // get intent that started this activity and extract item id
        Intent intent = getIntent();
        int itemId = intent.getIntExtra("ITEM_ID", 0);

        // set firebase references to pull item details
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
        itemRef = dbReference.child("Items").child(Integer.toString(itemId));

        // set View references
        setViewRefs();

        // get item from database
        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                item = dataSnapshot.getValue(Item.class);

                // set item name on display
                itemName_txt.setText(item.getName());

                // TODO: add category attribute in class + edit database items

                // set recyclable icon
                int recyclability = item.getRecyclability();
                if (recyclability == 2 || recyclability == 1) {
                    recyclable_icon.setImageResource(R.drawable.checked);
                    recyclable_txt.setText(R.string.Det_txt_recyclable);

                    // get indiv bin

                }
                else {  // not recyclable
                    recyclable_icon.setImageResource(R.drawable.cross);
                    recyclable_txt.setText(R.string.Det_txt_notRecyclable);
                    recyclable_txt.setTextColor(Color.parseColor("#F05228"));
                    howto_txt.setText(R.string.Det_whattodo);
                    hdb_icon.setImageResource(R.drawable.cross);
                    sep_icon.setImageResource(R.drawable.cross);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        // set click listener for BACK TO HOME
        setBackToHomeClickListener();

    }

    private void setViewRefs() {
        itemName_txt = (TextView) findViewById(R.id.Det_item_name_txt);
        recyclable_icon = (ImageView) findViewById(R.id.Det_recyclable_icon);
        recyclable_txt = (TextView) findViewById(R.id.Det_recyclable_txt);
        howto_txt = (TextView) findViewById(R.id.Det_how_txt);
        hdb_icon = (ImageView) findViewById(R.id.Det_hdb_result);
        sep_icon = (ImageView) findViewById(R.id.Det_sep_result);
        home_btn = (ImageButton) findViewById(R.id.Det_backtohome_btn);
    }

    private void setBackToHomeClickListener() {
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailsPage.this, HomePage_loggedin.class);
                startActivity(intent);
            }
        });
    }
}

