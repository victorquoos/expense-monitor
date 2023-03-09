package com.ifsc.expensemonitor;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        List<MonthYear> months = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.YEAR, 1900);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
        endCalendar.add(Calendar.YEAR, 50);

        while (calendar.before(endCalendar)) {
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            if (calendar.get(Calendar.MONTH) == Calendar.JANUARY){
                months.add(new MonthYear(-1, year));
            }
            months.add(new MonthYear(month, year));

            calendar.add(Calendar.MONTH, 1);
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        int currentYearIndex = -1;
        for (int i = 0; i < months.size(); i++) {
            MonthYear monthYear = months.get(i);
            if (monthYear.getMonth() == -1 && monthYear.getYear() == currentYear) {
                currentYearIndex = i;
                break;
            }
        }

        if (currentYearIndex != -1) {
            recyclerView.scrollToPosition(currentYearIndex);
        }

        List<MonthYear> monthsYears = new ArrayList<>();

        MonthYearAdapter adapter = new MonthYearAdapter(months);
        recyclerView.setAdapter(adapter);

    }
}
