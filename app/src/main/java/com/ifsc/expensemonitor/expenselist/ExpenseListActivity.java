package com.ifsc.expensemonitor.expenselist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

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
import java.util.ArrayList;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    DatabaseReference monthRef;
    RecyclerView recyclerView;
    FloatingActionButton addExpenseButton;

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        addExpenseButton = findViewById(R.id.addExpenseButton);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.topAppBar);

        Intent intent = getIntent();
        int month = intent.getIntExtra("month", 0);
        int year = intent.getIntExtra("year", 0);

        setToolBarText(month, year);

        addExpenseButton.setOnClickListener(v -> newExpenseActivity(month, year));

        monthRef = FirebaseSettings.getUserReference()
                .child("expenses")
                .child("year" + year)
                .child("month" + month);
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

    private ValueEventListener expenseValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ExpenseCard> expenseCards = new ArrayList<>();
                for (DataSnapshot expenseData : snapshot.getChildren()) {
                    Expense expense = expenseData.getValue(Expense.class);
                    ExpenseCard expenseCard = new ExpenseCard();
                    expenseCard.setName(expense.getName());
                    expenseCard.setValue(expense.getValue());
                    expenseCard.setDate(expense.getCalendar().getTime());
                    expenseCard.setPaid(expense.isPaid());
                    expenseCards.add(expenseCard);
                }
                ExpenseCardAdapter adapter = new ExpenseCardAdapter(expenseCards);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
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