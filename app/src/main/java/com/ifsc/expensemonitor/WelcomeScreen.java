package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        expensesScreen();
        registerScreen();
    }

    private void expensesScreen() {
        Button nextPageButton = (Button) findViewById(R.id.withoutAccountBtn);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeScreen.this, ExpensesList.class));
            }
        });
    }

    private void registerScreen() {
        Button nextPageButton = (Button) findViewById(R.id.signupBtn);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeScreen.this, SignupScreen.class));
            }
        });
    }
}