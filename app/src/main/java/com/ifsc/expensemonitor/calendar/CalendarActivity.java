package com.ifsc.expensemonitor.calendar;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.expenselist.ExpenseListActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    // TODO: Make the initial year 1 year before the current year or the first year with expenses
    // TODO: Make the last year 1 year after the current year or the last year with expenses

    int firstYear = 2000;
    int yearsAhead = 10;
    int indexCurrentMonth = -1;
    RecyclerView recyclerView;
    int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_calendar);

        Intent thisIntent = getIntent();
        if (thisIntent.getBooleanExtra("isFromWelcome", false)) {
            Intent listIntent = new Intent(this, ExpenseListActivity.class);
            listIntent.putExtra("month", currentMonth);
            listIntent.putExtra("year", currentYear);
            startActivity(listIntent);
        }

        //recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        CustomSpanSizeLookup spanSizeLookup = new CustomSpanSizeLookup(getSapnCount());
        ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanSizeLookup(spanSizeLookup);





        //FloatingActionButton currentMonthBtn = findViewById(R.id.currentMonthBtn);
        //currentMonthBtn.setOnClickListener(view -> scrollToCurrentMonth());

        List<MonthYear> months = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.YEAR, firstYear);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
        endCalendar.add(Calendar.YEAR, yearsAhead);

        while (calendar.before(endCalendar)) {
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                months.add(new MonthYear(-1, year));
            }
            months.add(new MonthYear(month, year));

            calendar.add(Calendar.MONTH, 1);
        }

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












    private int getSapnCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int columnWidth = (int) getResources().getDimension(R.dimen.calendar_column_width);
        int screenWidth = displayMetrics.widthPixels;
        int spanCount = screenWidth / columnWidth;
        if (spanCount < 1) {
            spanCount = 1;
        }
        return spanCount;
    }














    // TODO: Change to instant scroll?
    private void scrollToCurrentMonth() {
        if (indexCurrentMonth != -1) {
            LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_START;
                }
            };
            smoothScroller.setTargetPosition(indexCurrentMonth - (getVisibleItemCount() / 2) + 1);
            recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
        }
    }

    private int getVisibleItemCount() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        return lastVisiblePosition - firstVisiblePosition + 1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
