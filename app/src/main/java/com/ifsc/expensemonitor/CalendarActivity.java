package com.ifsc.expensemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    int indexCurrentMonth = -1;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        FloatingActionButton currentMonthBtn = findViewById(R.id.currentMonthBtn);
        currentMonthBtn.setOnClickListener(view -> scrollToCurrentMonth());

        recyclerView = findViewById(R.id.recycler_view);

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

            if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                months.add(new MonthYear(-1, year));
            }
            months.add(new MonthYear(month, year));

            calendar.add(Calendar.MONTH, 1);
        }

        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        indexCurrentMonth = -1;
        for (int i = 0; i < months.size(); i++) {
            MonthYear monthYear = months.get(i);

            if (monthYear.getMonth() == currentMonth && monthYear.getYear() == currentYear) {
                monthYear.setCurrentMonth(true);
                indexCurrentMonth = i;
                break;
            }
        }

        if (indexCurrentMonth != -1) {
            recyclerView.scrollToPosition(indexCurrentMonth);
        }

        MonthYearAdapter adapter = new MonthYearAdapter(months);
        recyclerView.setAdapter(adapter);
    }

    private void scrollToCurrentMonth() {
        if (indexCurrentMonth != -1) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

            int recyclerViewHeight = recyclerView.getHeight();

            View firstVisibleItem = layoutManager.findViewByPosition(firstVisibleItemPosition);
            int firstVisibleItemHeight = firstVisibleItem.getHeight();

            View lastVisibleItem = layoutManager.findViewByPosition(lastVisibleItemPosition);
            int lastVisibleItemHeight = lastVisibleItem.getHeight();

            int totalVisibleItemsHeight = lastVisibleItem.getBottom() - firstVisibleItem.getTop();

            int offset;
            if (totalVisibleItemsHeight >= recyclerViewHeight) {
                offset = (recyclerViewHeight - firstVisibleItemHeight) / 2;
            } else {
                offset = (recyclerViewHeight - totalVisibleItemsHeight) / 2 - firstVisibleItem.getTop();
            }

            layoutManager.scrollToPositionWithOffset(indexCurrentMonth, offset);
        }
    }



}
