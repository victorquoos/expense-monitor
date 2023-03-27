package com.ifsc.expensemonitor.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.calendar.CalendarActivity;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.User;

import java.util.Date;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private Button signupButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(view -> signup());

    }

    private void signup() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show(); // TODO: Make a string resource
        } else {
            createUser(email, password);
        }
    }

    private void createUser(String email, String password) {
        auth = FirebaseSettings.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                User user = new User();
                user.setUid(auth.getUid());
                user.setEmail(email);
                user.setPassword(password);
                user.save();

                Toast.makeText(this, "Usuário criado com sucesso", Toast.LENGTH_SHORT).show(); // TODO: Make a string resource
                finish();
            } else {
                String exception;
                try {
                    throw task.getException();
                } catch (FirebaseAuthWeakPasswordException e) {
                    exception = "Senha fraca"; // TODO: Make a string resource
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    exception = "Email inválido"; // TODO: Make a string resource
                } catch (FirebaseAuthUserCollisionException e) {
                    exception = "Email já cadastrado"; // TODO: Make a string resource
                } catch (Exception e) {
                    exception = "Erro ao cadastrar usuário: " + e.getMessage(); // TODO: Make a string resource
                }
                Toast.makeText(this, exception, Toast.LENGTH_SHORT).show();
            }
        });

    }
}