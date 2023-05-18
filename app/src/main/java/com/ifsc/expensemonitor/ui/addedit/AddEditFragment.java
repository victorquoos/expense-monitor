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

import java.util.Calendar;

public class AddEditFragment extends Fragment {

    private AddEditViewModel mViewModel;
    private MaterialToolbar materialToolbar;
    private EditText expenseValueEditText, expenseNameEditText, expenseDescriptionEditText, expenseDateEditText;
    private ExtendedFloatingActionButton saveExpenseButton;
    private SimpleDate selectedDate;

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
        int month = AddEditFragmentArgs.fromBundle(getArguments()).getMonth();
        int year = AddEditFragmentArgs.fromBundle(getArguments()).getYear();
        String key = AddEditFragmentArgs.fromBundle(getArguments()).getKey();

        // Configuração dos valores padrão
        if (key.isEmpty()) {
            selectedDate = SimpleDate.getCurrentDate();
            if (month != Calendar.getInstance().get(Calendar.MONTH) || year != Calendar.getInstance().get(Calendar.YEAR)) {
                selectedDate.setMonth(month);
                selectedDate.setYear(year);
                selectedDate.setDay(1);
            }
            expenseDateEditText.setText(selectedDate.getFormattedDate());
        } else {
            Expense expense = FirebaseSettings.getExpense(month, year, key);
            expenseValueEditText.setText(String.valueOf(expense.getValue()));
            expenseNameEditText.setText(expense.getName());
            expenseDescriptionEditText.setText(expense.getDescription());
            selectedDate = expense.getDate();
            expenseDateEditText.setText(selectedDate.getFormattedDate());
        }

        // Configuração da toolbar
        materialToolbar.setTitle("Adicionar despesa");
        materialToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        materialToolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).navigateUp());


        //TODO: Implementar a lógica de salvar a despesa


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddEditViewModel.class);
    }

}