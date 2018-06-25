package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ItemDetailsPage extends AppCompatActivity {
    private ImageButton home_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_page);



        // set click listener for BACK TO HOME
        home_btn = (ImageButton) findViewById(R.id.Det_backtohome_btn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailsPage.this, HomePage_loggedin.class);
                startActivity(intent);
            }
        });
    }
}
