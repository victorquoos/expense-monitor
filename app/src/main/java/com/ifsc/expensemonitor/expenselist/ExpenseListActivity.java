package com.ifsc.expensemonitor.expenselist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.ifsc.expensemonitor.R;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        // Intent to get the month and year from MonthYearAdapter.onBindViewHolder
        Intent intent = getIntent();
        String month = intent.getStringExtra("month");
        String year = intent.getStringExtra("year");

        // Apply the month and year to the menu bar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(month);
        toolbar.setSubtitle(year);

        // TODO: Create a method for the floating action button

        recyclerView = findViewById(R.id.recyclerView);

        List<ExpenseCard> expenses = new ArrayList<>();




    }
}