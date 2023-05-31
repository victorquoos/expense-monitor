package com.ifsc.expensemonitor.ui.pager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.ui.monthselector.MonthYear;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class PagerViewModel extends ViewModel {
    private MutableLiveData<List<MonthYear>> listOfMonths;
    private MutableLiveData<MonthYear> lastVisibleMonthYear;
    private MutableLiveData<Integer> initialPageIndex;
    private MutableLiveData<Integer> targetPageIndex;
    private MutableLiveData<MonthYear> targetMonthYear;
    private int currentMonthIndex;
    private boolean isFirstTime;


    public PagerViewModel() {
        listOfMonths = new MutableLiveData<>();
        initialPageIndex = new MutableLiveData<>(); // talvez não precise ser MutableLiveData
        lastVisibleMonthYear = new MutableLiveData<>();
        targetPageIndex = new MutableLiveData<>();
        targetMonthYear = new MutableLiveData<>();
        currentMonthIndex = 0;
        isFirstTime = true;

        startListenerToGetYears();
    }

    public MutableLiveData<List<MonthYear>> getListOfMonths() {
        return listOfMonths;
    }

    public MutableLiveData<Integer> getInitialPageIndex() {
        return initialPageIndex;
    }

    public MutableLiveData<MonthYear> getLastVisibleMonthYear() {
        return lastVisibleMonthYear;
    }

    public MutableLiveData<Integer> getTargetPageIndex() {
        return targetPageIndex;
    }

    public MutableLiveData<MonthYear> getTargetMonthYear() {
        return targetMonthYear;
    }


    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    public int getCurrentMonthIndex() {
        return currentMonthIndex;
    }


    public void startListenerToGetYears() {
        DatabaseReference yearsReference = FirebaseSettings.getUserReference().child("expenses");

        yearsReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int firstYearOfList, lastYearOfList;

                // Cria a lista de anos
                List<Integer> yearsList = new ArrayList<>();
                for (DataSnapshot year : snapshot.getChildren()) {
                    String yearText = year.getKey();
                    if (yearText != null && yearText.startsWith("year")) {
                        int yearNumber = Integer.parseInt(yearText.substring(4));
                        yearsList.add(yearNumber);
                    }
                }

                // Define o primeiro e último ano da lista de meses
                Collections.sort(yearsList);
                if (!yearsList.isEmpty()) {
                    firstYearOfList = Math.min(yearsList.get(0), currentYear - 1);
                    lastYearOfList = Math.max(yearsList.get(yearsList.size() - 1), currentYear + 1);
                } else {
                    firstYearOfList = currentYear - 1;
                    lastYearOfList = currentYear + 1;
                }

                // Cria a lista de meses
                currentMonthIndex = 0;
                List<MonthYear> monthYearList = new ArrayList<>();
                for (int year = firstYearOfList; year <= lastYearOfList; year++) {
                    for (int month = 0; month < 12; month++) {
                        if (year == currentYear && month == currentMonth) {
                            currentMonthIndex = monthYearList.size();
                            monthYearList.add(new MonthYear(month, year, true));
                            continue;
                        }
                        monthYearList.add(new MonthYear(month, year));
                    }
                }
                initialPageIndex.setValue(currentMonthIndex);

                // Atualiza os valores
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
