package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class FindItemsPage extends AppCompatActivity {
    private Button category_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_items_page);

        // click listeners for categories button
        category_btn = (Button) findViewById(R.id.FI_categories_btn);
        category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryPage();
            }
        });
    }

    public void openCategoryPage() {
        Intent intent = new Intent(this, temp_toremove.class);
        startActivity(intent);
    }
}
