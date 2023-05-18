package com.ifsc.expensemonitor.ui.addedit;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.Expense;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.SimpleDate;
import com.ifsc.expensemonitor.ui.start.WelcomeViewModel;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class AddEditFragment extends Fragment {

    private AddEditViewModel mViewModel;
    private MaterialToolbar materialToolbar;
    private EditText expenseValueEditText, expenseNameEditText, expenseDescriptionEditText, expenseDateEditText;
    private ExtendedFloatingActionButton saveExpenseButton;
    private SimpleDate selectedDate;
    private String key;
    int month, year;

    public static AddEditFragment newInstance() {
        return new AddEditFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        // Declaração dos componentes da tela
        materialToolbar = view.findViewById(R.id.materialToolbar);
        expenseValueEditText = view.findViewById(R.id.expenseValueEditText);
        expenseNameEditText = view.findViewById(R.id.expenseNameEditText);
        expenseDescriptionEditText = view.findViewById(R.id.expenseDescriptionEditText);
        expenseDateEditText = view.findViewById(R.id.expenseDateEditText);
        saveExpenseButton = view.findViewById(R.id.saveExpenseButton);

        // Recebendo os dados da tela anterior
        month = AddEditFragmentArgs.fromBundle(getArguments()).getMonth();
        year = AddEditFragmentArgs.fromBundle(getArguments()).getYear();
        key = AddEditFragmentArgs.fromBundle(getArguments()).getKey();

        //TODO: Implementar a lógica de salvar a despesa


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddEditViewModel.class);

        // Configuração dos valores padrão
        if (key.isEmpty() || key == null) {
            selectedDate = SimpleDate.getCurrentDate();
            if (month != Calendar.getInstance().get(Calendar.MONTH) || year != Calendar.getInstance().get(Calendar.YEAR)) {
                selectedDate.setMonth(month);
                selectedDate.setYear(year);
                selectedDate.setDay(1);
            }
            expenseDateEditText.setText(selectedDate.getFormattedDate());
        } else {
            mViewModel.loadExpenseData(month, year, key);
            mViewModel.getExpense().observe(getViewLifecycleOwner(), expense -> {
                expenseValueEditText.setText(String.valueOf(expense.getValue()));
                expenseNameEditText.setText(expense.getName());
                expenseDescriptionEditText.setText(expense.getDescription());
                selectedDate = expense.getDate();
                expenseDateEditText.setText(selectedDate.getFormattedDate());
            });
        }
    }
}