package com.ifsc.expensemonitor.ui.monthlist;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.database.FirebaseSettings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonthSelectorViewModel extends ViewModel {
    private MutableLiveData<Integer> firstYear;
    private MutableLiveData<Integer> lastYear;
    private MutableLiveData<List<MonthYear>> months;
    private int currentYear, currentMonth;
    private int indexOfCurrentMonth = -1;

    public MonthSelectorViewModel() {
        firstYear = new MutableLiveData<>();
        lastYear = new MutableLiveData<>();
        months = new MutableLiveData<>();

        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        currentMonth = Calendar.getInstance().get(Calendar.MONTH);

        firstYear.setValue(currentYear -1);
        lastYear.setValue(currentYear +1);

        loadMonthList();
    }

    public MutableLiveData<Integer> getFirstYear() {
        return firstYear;
    }

    public MutableLiveData<Integer> getLastYear() {
        return lastYear;
    }

    public int getIndexOfCurrentMonth() {
        return indexOfCurrentMonth;
    }

    public MutableLiveData<List<MonthYear>> getMonthList() {
        return months;
    }

    public void loadMonthList() {
        DatabaseReference yearsReference = FirebaseSettings.getUserReference().child("expenses");

        yearsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Integer> years = new ArrayList<>();
                for (DataSnapshot year : snapshot.getChildren()) {
                    String yearText = year.getKey();
                    Pattern pattern = Pattern.compile("year(\\d{4})");
                    Matcher matcher = pattern.matcher(yearText);

                    if (matcher.find()) {
                        String anoString = matcher.group(1);
                        int yearNumber = Integer.parseInt(anoString);
                        years.add(yearNumber);
                    }
                }

                Collections.sort(years);

                if (!years.isEmpty()) {
                    int firstYearValue = Math.min(years.get(0), currentYear-1);
                    int lastYearValue = Math.max(years.get(years.size()-1), currentYear+1);
                    firstYear.setValue(firstYearValue);
                    lastYear.setValue(lastYearValue);

                    List<MonthYear> monthList = new ArrayList<>();
                    while (firstYearValue <= lastYearValue) {
                        for (int i = 0; i < 12; i++) {
                            if (i == Calendar.JANUARY) {
                                monthList.add(new MonthYear(-1, firstYearValue));
                            }
                            MonthYear monthYear = new MonthYear(i, firstYearValue);
                            monthYear.setCurrentMonth(false);

                            monthList.add(monthYear);
                            if (firstYearValue == currentYear && i == currentMonth) {
                                indexOfCurrentMonth = monthList.size() - 1;
                                monthYear.setCurrentMonth(true);
                            }
                        }
                        firstYearValue++;
                    }
                    months.setValue(monthList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MonthSelectorViewModel", "Erro ao obter anos", error.toException());
            }
        });
    }


}