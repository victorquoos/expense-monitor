package com.ifsc.expensemonitor.addeditexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.Expense;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.SimpleDate;

import java.util.Calendar;

public class NewExpenseActivity extends AppCompatActivity {

    EditText expenseName, expenseValue, expenseDate, expenseDescription;
    ExtendedFloatingActionButton saveButton;
    SimpleDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        expenseName = findViewById(R.id.expenseName); //TODO: Rename ids
        expenseValue = findViewById(R.id.expenseValue); //TODO: Mask for currency
        expenseDate = findViewById(R.id.expenseDate);
        expenseDescription = findViewById(R.id.expenseDescription);
        saveButton = findViewById(R.id.saveButton);

        Intent intent = getIntent();
        int month = intent.getIntExtra("month", Calendar.getInstance().get(Calendar.MONTH));
        int year = intent.getIntExtra("year", Calendar.getInstance().get(Calendar.YEAR));

        selectedDate = SimpleDate.getCurrentDate();
        if (month != Calendar.getInstance().get(Calendar.MONTH) || year != Calendar.getInstance().get(Calendar.YEAR)) {
            selectedDate.setMonth(month);
            selectedDate.setYear(year);
            selectedDate.setDay(1);
        }
        expenseDate.setText(selectedDate.getFormattedDate());

        expenseDate.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Selecione uma data"); //TODO: make a string resource
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
            MaterialDatePicker<Long> datePicker = builder.build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDate.setDate(selection);
                expenseDate.setText(selectedDate.getFormattedDate());
            });

            datePicker.show(getSupportFragmentManager(), datePicker.toString());
        });

        saveButton.setOnClickListener(v -> { //TODO: check every field before return
            if (expenseValue.getText().toString().isEmpty()) {
                expenseValue.setError("O valor da despesa não pode estar vazio"); //TODO: make a string resource
            } else if (expenseDate.getText().toString().isEmpty()) {
                expenseDate.setError("A data da despesa não pode estar vazia"); //TODO: make a string resource
            } else if (expenseName.getText().toString().isEmpty()) { // TODO: maybe this field should be empty
                expenseName.setError("O nome da despesa não pode estar vazio"); //TODO: make a string resource
            } else {
                Expense expense = new Expense();
                expense.setName(expenseName.getText().toString());
                expense.setValue(Double.parseDouble(expenseValue.getText().toString()));
                expense.setDate(selectedDate);
                expense.setDescription(expenseDescription.getText().toString());
                FirebaseSettings.saveExpense(expense);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
