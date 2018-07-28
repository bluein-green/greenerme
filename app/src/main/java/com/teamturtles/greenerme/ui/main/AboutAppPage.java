package com.teamturtles.greenerme.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamturtles.greenerme.R;

public class AboutAppPage extends AppCompatActivity {

    private ImageView homepage_loggedout_logo;
    private TextView title_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app_page);

        homepage_loggedout_logo = (ImageView) findViewById(R.id.homepage_loggedout_logo);
        homepage_loggedout_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutAppPage.this, HomePage_loggedin_v2.class));
                finish();
            }
        });

        title_txt = (TextView) findViewById(R.id.about_title);
        title_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutAppPage.this, HomePage_loggedin_v2.class));
                finish();
            }
        });
    }
}
