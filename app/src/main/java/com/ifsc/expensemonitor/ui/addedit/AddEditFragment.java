package com.ifsc.expensemonitor.ui.addedit;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.Expense;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.MoneyValue;
import com.ifsc.expensemonitor.database.SimpleDate;
import com.ifsc.expensemonitor.ui.monthlist.MonthYear;
import com.ifsc.expensemonitor.ui.pager.PagerViewModel;

import java.text.NumberFormat;
import java.util.Calendar;

public class AddEditFragment extends Fragment {
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private AddEditViewModel mViewModel;
    private MaterialToolbar materialToolbar;
    private EditText expenseValueEditText, expenseNameEditText, expenseDescriptionEditText, expenseDateEditText;
    private TextView expenseValueTextView;
    private ExtendedFloatingActionButton saveExpenseButton;
    int month, year;
    private String key;
    SimpleDate selectedDate;

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
        expenseValueTextView = view.findViewById(R.id.expenseValueTextView);

        // Recuperando os dados da tela anterior
        month = AddEditFragmentArgs.fromBundle(getArguments()).getMonth();
        year = AddEditFragmentArgs.fromBundle(getArguments()).getYear();
        key = AddEditFragmentArgs.fromBundle(getArguments()).getKey();

        // Configuração da toolbar
        materialToolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        // Configuração do textwatcher do valor da despesa
        expenseValueEditText.addTextChangedListener(expenseValueEditTextWatcher);

        // Configuração do datepicker
        expenseDateEditText.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Selecione uma data"); //TODO: make a string resource
            builder.setSelection(selectedDate.getDateInMillis());
            MaterialDatePicker<Long> datePicker = builder.build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDate.setDate(selection);
                expenseDateEditText.setText(selectedDate.getFormattedDate());
            });

            datePicker.show(getParentFragmentManager(), datePicker.toString());
        });

        // Lógica de salvar a despesa
        saveExpenseButton.setOnClickListener(v -> {
            long value = 0L;
            if (!expenseValueEditText.getText().toString().isEmpty()) {
                value = Long.parseLong(expenseValueEditText.getText().toString());
            }
            String name = expenseNameEditText.getText().toString();
            String description = expenseDescriptionEditText.getText().toString();
            SimpleDate date = selectedDate;

            if (name.isEmpty()) {
                expenseNameEditText.setError("Insira um nome válido");
                expenseNameEditText.requestFocus();
            } else if (date == null) {
                expenseDateEditText.setError("Insira uma data válida");
                expenseDateEditText.requestFocus();
            } else {
                if (mViewModel.getExpense().getValue() == null) {
                    Expense expense = new Expense(name, value, date, description);
                    FirebaseSettings.saveExpense(expense);
                } else {
                    Expense expense = new Expense(name, value, date, description);
                    FirebaseSettings.updateExpense(mViewModel.getExpense().getValue(), expense);
                }
                PagerViewModel pagerViewModel = new ViewModelProvider(requireActivity()).get(PagerViewModel.class);
                pagerViewModel.getTargetMonthYear().setValue(new MonthYear(selectedDate.getMonth(), selectedDate.getYear()));
                Navigation.findNavController(view).navigateUp();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddEditViewModel.class);

        // Configuração dos valores iniciais
        setInitialValues(month, year, key, mViewModel);

        // Atualiza os dados de acordo com a despesa selecionada no viewModel
        mViewModel.getExpense().observe(getViewLifecycleOwner(), expense -> {
            if (expense != null) {
                expenseValueEditText.setText(String.valueOf(expense.getValue()));
                expenseNameEditText.setText(expense.getName());
                expenseDescriptionEditText.setText(expense.getDescription());
                expenseDateEditText.setText(expense.getDate().getFormattedDate());
                selectedDate = expense.getDate();
            }
        });
    }

    public void setInitialValues(int month, int year, String key, AddEditViewModel mViewModel) {
        selectedDate = SimpleDate.getCurrentDate();
        expenseValueEditText.setText("0");
        if (key.isEmpty()) {
            materialToolbar.setTitle("Adicionar despesa");
            if (month != Calendar.getInstance().get(Calendar.MONTH) || year != Calendar.getInstance().get(Calendar.YEAR)) {
                selectedDate.setMonth(month);
                selectedDate.setYear(year);
                selectedDate.setDay(1);
            }
            expenseDateEditText.setText(selectedDate.getFormattedDate());
        } else {
            materialToolbar.setTitle("Editar despesa");
            mViewModel.loadExpenseData(month, year, key);
        }
    }

    private final TextWatcher expenseValueEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String string = s.toString();

            if (string.startsWith("0")) {
                string = string.replaceAll("^0+", "");
                expenseValueEditText.setText(string);
            }
            try {
                expenseValueTextView.setText(MoneyValue.format(string));
            } catch (NumberFormatException e) {
                expenseValueTextView.setText(MoneyValue.format(0));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}