package com.ifsc.expensemonitor.ui.settings;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.auth.FirebaseAuth;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.data.FirebaseSettings;
import com.ifsc.expensemonitor.data.PreferenceUtils;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    MaterialToolbar materialToolbar;
    private TextView exitAccount;
    private MaterialSwitch darkModeSwitch;
    private PreferenceUtils preferenceUtils;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        preferenceUtils = new PreferenceUtils(getContext());

        materialToolbar = view.findViewById(R.id.materialToolbar);
        exitAccount = view.findViewById(R.id.exitAccount);
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);

        // inicializa o switch do modo escuro com o valor armazenado no sharedpreferences
        darkModeSwitch.setChecked(preferenceUtils.getDarkMode());

        //back button
        materialToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        materialToolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        exitAccount.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            // restart app
            requireActivity().finish();
            startActivity(requireActivity().getIntent());
        });

        // Listener para o switch do modo escuro
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Salva o estado do modo escuro no sharedpreferences quando o estado do switch mudar
            preferenceUtils.setDarkMode(isChecked);

            // reinitialize app
            requireActivity().finish();
            startActivity(requireActivity().getIntent());
        });

        return view;
    }
}
