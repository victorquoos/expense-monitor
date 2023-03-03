package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarActivity extends AppCompatActivity {


    private MonthsAdapter monthsAdapter;
    private List<Calendar> months = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Obtém a data atual
        Calendar today = Calendar.getInstance();

        // Define a data mínima (1º de janeiro de 1950)
        Calendar minimumDate = Calendar.getInstance();
        minimumDate.set(1950, Calendar.JANUARY, 1);

        // Define a data máxima (31 de dezembro do year atual + 100 years)
        Calendar maximumDate = Calendar.getInstance();
        maximumDate.set(today.get(Calendar.YEAR) + 100, Calendar.DECEMBER, 31);

        // Itera por todos os monthes entre a data mínima e a máxima
        while (!minimumDate.after(maximumDate)) {
            months.add((Calendar) minimumDate.clone());
            minimumDate.add(Calendar.MONTH, 1);
        }

        monthsAdapter = new MonthsAdapter(months);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(monthsAdapter);

    }

}

