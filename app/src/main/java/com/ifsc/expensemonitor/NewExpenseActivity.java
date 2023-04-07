package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ifsc.expensemonitor.database.Expense;
import com.ifsc.expensemonitor.database.FirebaseSettings;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewExpenseActivity extends AppCompatActivity {

    EditText expenseName, expenseValue, expenseDate, expenseDescription;
    ExtendedFloatingActionButton saveButton;
    Calendar selectedDate;
    String formattedDate;

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
        int month = intent.getIntExtra("month", 0);
        int year = intent.getIntExtra("year", 0);

        if (month == Calendar.getInstance().get(Calendar.MONTH) && year == Calendar.getInstance().get(Calendar.YEAR)) {
            selectedDate = Calendar.getInstance();
        } else {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, 1);
        }
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        formattedDate = dateFormat.format(selectedDate.getTime());
        expenseDate.setText(formattedDate);

        expenseDate.setOnClickListener(v -> {

            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Selecione uma data"); //TODO: make a string resource
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
            MaterialDatePicker<Long> datePicker = builder.build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDate.setTime(new Date(selection));
                formattedDate = dateFormat.format(selectedDate.getTime());
                expenseDate.setText(formattedDate);
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
                expense.setYear(selectedDate.get(Calendar.YEAR));
                expense.setMonth(selectedDate.get(Calendar.MONTH));
                expense.setDay(selectedDate.get(Calendar.DAY_OF_MONTH));
                expense.setDescription(expenseDescription.getText().toString());
                FirebaseSettings.saveExpense(expense);
            }
        });
    }
}
