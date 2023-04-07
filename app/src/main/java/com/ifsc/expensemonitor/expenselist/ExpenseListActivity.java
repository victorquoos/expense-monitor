package com.ifsc.expensemonitor.expenselist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.NewExpenseActivity;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.database.Expense;
import com.ifsc.expensemonitor.database.FirebaseSettings;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    DatabaseReference monthRef;
    FloatingActionButton addExpenseButton;
    MaterialToolbar toolbar;
    RecyclerView exepensesReciclerView;
    TextView paidValueTextView, pendingValueTextView, totalValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int month = intent.getIntExtra("month", 0);
        int year = intent.getIntExtra("year", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        addExpenseButton = findViewById(R.id.addExpenseButton);
        exepensesReciclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.topAppBar);
        paidValueTextView = findViewById(R.id.paidValueTextView);
        pendingValueTextView = findViewById(R.id.pendingValueTextView);
        totalValueTextView = findViewById(R.id.totalValueTextView);

        toolbar.setNavigationOnClickListener(view -> finish());
        setToolBarText(month, year);

        addExpenseButton.setOnClickListener(v -> newExpenseActivity(month, year));

        monthRef = FirebaseSettings.getMonthReference(year, month);
    }

    private void newExpenseActivity(int month, int year) {
        Intent newExpenseIntent = new Intent(this, NewExpenseActivity.class);
        newExpenseIntent.putExtra("month", month);
        newExpenseIntent.putExtra("year", year);
        startActivity(newExpenseIntent);
    }

    private void setToolBarText(int month, int year) {
        String monthText = new DateFormatSymbols().getMonths()[month];
        monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1).toLowerCase();
        String yearText = String.valueOf(year);
        toolbar.setTitle(monthText);
        toolbar.setSubtitle(yearText);
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
                    assert expense != null;
                    expense.setKey(expenseData.getKey());
                    expenseCards.add(expense);
                    if (expense.isPaid()) {
                        totalPaid += expense.getValue();
                    } else {
                        totalPending += expense.getValue();
                    }
                }
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