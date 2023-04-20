package com.ifsc.expensemonitor.expenselist;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.NewExpenseActivity;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.calendar.CalendarActivity;
import com.ifsc.expensemonitor.database.Expense;
import com.ifsc.expensemonitor.database.FirebaseSettings;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    int month, year;
    DatabaseReference monthRef;
    FloatingActionButton addExpenseButton;
    RecyclerView exepensesReciclerView;
    TextView paidValueTextView, pendingValueTextView, totalValueTextView, monthTextView, yearTextView;
    Button filtersButton, optionsButton, previousMonthButton, nextMonthButton, monthYearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        addExpenseButton = findViewById(R.id.addExpenseButton);
        exepensesReciclerView = findViewById(R.id.recyclerView);
        paidValueTextView = findViewById(R.id.paidValueTextView);
        pendingValueTextView = findViewById(R.id.pendingValueTextView);
        totalValueTextView = findViewById(R.id.totalValueTextView);
        monthTextView = findViewById(R.id.monthTextView);
        yearTextView = findViewById(R.id.yearTextView);
        filtersButton = findViewById(R.id.filtersButton);
        optionsButton = findViewById(R.id.optionsButton);
        previousMonthButton = findViewById(R.id.previousMonthButton);
        nextMonthButton = findViewById(R.id.nextMonthButton);
        monthYearButton = findViewById(R.id.monthYearButton);

        monthYearButton.setOnClickListener(v -> goToCalendarActivity());
        nextMonthButton.setOnClickListener(v -> goToNextMonth());
        previousMonthButton.setOnClickListener(v -> goToPreviousMonth());
        addExpenseButton.setOnClickListener(v -> newExpenseActivity());

        if (savedInstanceState != null) {
            month = savedInstanceState.getInt("month");
            year = savedInstanceState.getInt("year");
            setMonth(month, year);
        } else {
            Intent intent = getIntent();
            month = intent.getIntExtra("month", 0);
            year = intent.getIntExtra("year", 0);
            setMonth(month, year);
        }
    }


    private void goToCalendarActivity() {
        Intent calendarIntent = new Intent(this, CalendarActivity.class);
        startActivity(calendarIntent);
    }

    private void goToNextMonth() {
        if (month == 11) {
            month = 0;
            year++;
        } else {
            month++;
        }
        setMonth(month, year);
        restartListener();
    }

    private void goToPreviousMonth() {
        if (month == 0) {
            month = 11;
            year--;
        } else {
            month--;
        }
        setMonth(month, year);
        restartListener();
    }

    private void setMonth(int month, int year) {
        monthRef = FirebaseSettings.getMonthReference(year, month);
        setToolBarText(month, year);
    }

    private void restartListener() {
        monthRef.removeEventListener(expenseValueEventListener());
        monthRef.addValueEventListener(expenseValueEventListener());
    }

    private void newExpenseActivity() {
        Intent newExpenseIntent = new Intent(this, NewExpenseActivity.class);
        newExpenseIntent.putExtra("month", month);
        newExpenseIntent.putExtra("year", year);
        startActivity(newExpenseIntent);
    }

    private void setToolBarText(int month, int year) {
        String monthText = new DateFormatSymbols().getMonths()[month];
        monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1).toLowerCase();
        String yearText = String.valueOf(year);
        monthTextView.setText(monthText);
        yearTextView.setText(yearText);
    }

    private ValueEventListener expenseValueEventListener() { //TODO: move to FirebaseSettings
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Expense> expenseCards = new ArrayList<>();
                double totalPaid = 0.0;
                double totalPending = 0.0;
                for (DataSnapshot expenseData : snapshot.getChildren()) {
                    Expense expense = expenseData.getValue(Expense.class);
                    expense.setKey(expenseData.getKey());
                    expenseCards.add(expense);
                    if (expense.isPaid()) {
                        totalPaid += expense.getValue();
                    } else {
                        totalPending += expense.getValue();
                    }
                }

                Collections.sort(expenseCards, (o1, o2) -> o1.getDate().compareTo(o2.getDate())); //TODO: create other sorting methods

                ExpenseCardAdapter adapter = new ExpenseCardAdapter(expenseCards);
                exepensesReciclerView.setAdapter(adapter);
                setTotals(totalPaid, totalPending);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private void setTotals(double totalPaid, double totalPending) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        String paidValue = currencyFormat.format(totalPaid);
        paidValueTextView.setText(paidValue);

        String pendingValue = currencyFormat.format(totalPending);
        pendingValueTextView.setText(pendingValue);

        double totalValue = totalPaid + totalPending;
        String totalValueString = currencyFormat.format(totalValue);
        totalValueTextView.setText(totalValueString);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("month", month);
        outState.putInt("year", year);
    }

    @Override
    protected void onStart() {
        super.onStart();
        monthRef.addValueEventListener(expenseValueEventListener());
    }

    @Override
    protected void onStop() {
        super.onStop();
        monthRef.removeEventListener(expenseValueEventListener());
    }

}