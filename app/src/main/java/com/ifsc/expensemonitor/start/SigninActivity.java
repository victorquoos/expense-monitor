package com.ifsc.expensemonitor.start;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.FirebaseSettings;

public class SigninActivity extends AppCompatActivity {
    private TextInputEditText emailEditText, passwordEditText;
    private Button signinButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signinButton = findViewById(R.id.signinButton);

        signinButton.setOnClickListener(view -> signin());
    }

    private void signin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show(); // TODO: Make a string resource
        } else {
            signin(email, password);
        }
    }

    private void signin(String email, String password) {
        auth = FirebaseSettings.getFirebaseAuth();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Usuário logado com sucesso", Toast.LENGTH_SHORT).show(); // TODO: Make a string resource
                finish();
            } else {
                String exception;
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    exception = "Usuário não cadastrado"; // TODO: Make a string resource
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    exception = "Email ou senha incorretos"; // TODO: Make a string resource
                } catch (Exception e) {
                    exception = "Erro ao logar usuário: " + e.getMessage(); // TODO: Make a string resource
                }
                Toast.makeText(this, exception, Toast.LENGTH_SHORT).show();
            }
        });
    }
}