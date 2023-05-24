package com.ifsc.expensemonitor.ui.pager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.ui.monthlist.MonthYear;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class PagerViewModel extends ViewModel {

    private MutableLiveData<List<MonthYear>> listOfMonths;
    private MutableLiveData<Integer> currentMonthIndex;
    private MutableLiveData<Integer> visiblePageIndex;


    public PagerViewModel() {
        listOfMonths = new MutableLiveData<>();
        currentMonthIndex = new MutableLiveData<>(); // talvez não precise ser MutableLiveData
        visiblePageIndex = new MutableLiveData<>();
    }

    public MutableLiveData<List<MonthYear>> getListOfMonths() {
        return listOfMonths;
    }

    public MutableLiveData<Integer> getCurrentMonthIndex() {
        return currentMonthIndex;
    }

    public MutableLiveData<Integer> getVisiblePageIndex() {
        return visiblePageIndex;
    }

    public int getVisibleMonth() {
        if (listOfMonths.getValue() == null || visiblePageIndex.getValue() == null) {
            return -1;
        }
        return listOfMonths.getValue().get(visiblePageIndex.getValue()).getMonth();
    }

    public int getVisibleYear() {
        if (listOfMonths.getValue() == null || visiblePageIndex.getValue() == null) {
            return -1;
        }
        return listOfMonths.getValue().get(visiblePageIndex.getValue()).getYear();
    }


    public void startListenerToGetYears() {
        DatabaseReference yearsReference = FirebaseSettings.getUserReference().child("expenses");

        yearsReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Integer> years = new ArrayList<>();
                for (DataSnapshot year : snapshot.getChildren()) {
                    String yearText = year.getKey();
                    if (yearText != null && yearText.startsWith("year")) {
                        int yearNumber = Integer.parseInt(yearText.substring(4));
                        years.add(yearNumber);
                    }
                }
                Collections.sort(years);

                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int firstYearOfList, lastYearOfList;

                if (!years.isEmpty()) {
                    firstYearOfList = Math.min(years.get(0), currentYear - 1);
                    lastYearOfList = Math.max(years.get(years.size() - 1), currentYear + 1);
                } else {
                    firstYearOfList = currentYear - 1;
                    lastYearOfList = currentYear + 1;
                }

                List<MonthYear> monthYearList = new ArrayList<>();
                for (int year = firstYearOfList; year <= lastYearOfList; year++) {
                    for (int month = 0; month < 12; month++) {
                        if (year == currentYear && month == currentMonth) {
                            currentMonthIndex.setValue(monthYearList.size());
                            monthYearList.add(new MonthYear(month, year, true));
                            continue;
                        }
                        monthYearList.add(new MonthYear(month, year));
                    }
                }

                if (listOfMonths.getValue() == null || !listOfMonths.getValue().equals(monthYearList)) {
                    listOfMonths.setValue(monthYearList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
