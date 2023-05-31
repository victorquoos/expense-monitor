package com.ifsc.expensemonitor.ui.start;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.FirebaseSettings;

public class WelcomeFragment extends Fragment {

    private WelcomeViewModel mViewModel;

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        // Declaração dos componentes da tela
        Button signupButton = view.findViewById(R.id.signupButton);
        Button signinButton = view.findViewById(R.id.signinButton);
        Button guestButton = view.findViewById(R.id.guestButton); // TODO: remover ou implementar

        // Ações dos botões
        signupButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_signUpFragment));
        signinButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_signInFragment));
        guestButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_pagerFragment);
                }
            });
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);

        // Se o usuário já estiver logado, vai direto para a lista de despesas
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_pagerFragment);
        }
    }
}