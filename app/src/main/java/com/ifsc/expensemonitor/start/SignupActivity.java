package com.ifsc.expensemonitor.start;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.FirebaseSettings;

public class SignupActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signupButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEditText = findViewById(R.id.email); //TODO arrumar o nome do id
        passwordEditText = findViewById(R.id.senha); //TODO arrumar o nome do id
        signupButton = findViewById(R.id.registerBtn); //TODO arrumar o nome do id


        signupButton.setOnClickListener(view -> signup());

    }

    public void signup() {
        auth = FirebaseSettings.getFirebaseAuth();
        auth.createUserWithEmailAndPassword("","").toString();
    }
}