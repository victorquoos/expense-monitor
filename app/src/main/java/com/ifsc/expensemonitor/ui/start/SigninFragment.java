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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.ifsc.expensemonitor.R;

public class SigninFragment extends Fragment {

    private SigninViewModel mViewModel;
    private FirebaseAuth auth;

    public static SigninFragment newInstance() {
        return new SigninFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        // Declaração dos componentes da tela
        TextInputEditText emailEditText = view.findViewById(R.id.emailEditText);
        TextInputEditText passwordEditText = view.findViewById(R.id.passwordEditText);
        Button signinButton = view.findViewById(R.id.signinButton);

        // Ações dos botões
        signinButton.setOnClickListener(v -> signin(emailEditText.getText().toString(), passwordEditText.getText().toString()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SigninViewModel.class);
    }

    private void signin(String email, String password) {
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Usuário logado com sucesso", Toast.LENGTH_SHORT).show(); // TODO: Make a string resource
                Navigation.findNavController(getView()).navigate(R.id.action_signInFragment_to_pagerFragment);
            } else {
                String exception;
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    exception = "Usuário não cadastrado"; // TODO: Make a string resource
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    exception = "Senha incorreta"; // TODO: Make a string resource
                } catch (Exception e) {
                    exception = "Erro ao logar usuário: " + e.getMessage(); // TODO: Make a string resource
                }
                Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
            }
        });
    }
}