package com.teamturtles.greenerme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgotAccPage extends AppCompatActivity {

    EditText email_forgotAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_acc_page);

        email_forgotAcc = (EditText) findViewById(R.id.email_forgotAcc);

        TextView loginText_btn2 = (TextView) findViewById(R.id.loginText_btn2);
        loginText_btn2.setOnClickListener(new View.OnClickListener() { // To click to the createe account page
            @Override
            public void onClick(View view) { // To click to the create account page
                Intent login_intent = new Intent(ForgotAccPage.this, LoginPage.class);
                ForgotAccPage.this.startActivity(login_intent);
            }
        });

        TextView inputEmail_btn = (TextView) findViewById(R.id.inputEmail_btn);
        inputEmail_btn.setOnClickListener(new View.OnClickListener() { // To click to the createe account page
            @Override
            public void onClick(View view) { // To click to the create account page
                checkEmail();
            }
        });
    }

    private void checkEmail() {

        String email = email_forgotAcc.getText().toString().trim();


        /*if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_forgotAcc.setError("This email has not been registered");
            email_forgotAcc.requestFocus();
        }
        */

    }

    public void toSignInPage (View view) { // created & send to the sign in page
        Intent startIntent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(startIntent);
    }
}
