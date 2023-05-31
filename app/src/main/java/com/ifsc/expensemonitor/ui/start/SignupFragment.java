package com.ifsc.expensemonitor.ui.start;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.User;

public class SignupFragment extends Fragment {

    private SignupViewModel mViewModel;
    private FirebaseAuth auth;

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // Declaração dos componentes da tela
        TextInputEditText emailEditText = view.findViewById(R.id.emailEditText);
        TextInputEditText passwordEditText = view.findViewById(R.id.passwordEditText);
        Button signupButton = view.findViewById(R.id.signupButton);

        // Ações dos botões
        signupButton.setOnClickListener(v -> signup(emailEditText.getText().toString(), passwordEditText.getText().toString()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
    }

    private void signup(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show(); // TODO: Make a string resource
        } else {
            auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User user = new User();
                    user.setUid(auth.getUid());
                    user.setEmail(email);
                    user.setPassword(password);
                    user.save();
                    Toast.makeText(getContext(), "Usuário criado com sucesso", Toast.LENGTH_SHORT).show(); // TODO: Make a string resource
                    Navigation.findNavController(getView()).navigate(R.id.action_signUpFragment_to_pagerFragment);
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
                    Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}