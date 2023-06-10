package com.ifsc.expensemonitor.ui.pager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
<<<<<<< HEAD
import com.ifsc.expensemonitor.data.FirebaseSettings;
import com.ifsc.expensemonitor.data.MonthYear;
import com.ifsc.expensemonitor.data.Occurrence;
import com.ifsc.expensemonitor.data.OccurrenceController;
=======
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.MonthYear;
<<<<<<< HEAD
>>>>>>> 5f814f6 (checkpoint)
=======
import com.ifsc.expensemonitor.database.Occurrence;
import com.ifsc.expensemonitor.database.OccurrenceController;
import com.ifsc.expensemonitor.database.SimpleDate;
>>>>>>> 9fbce0d (ajustes)

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class PagerViewModel extends ViewModel {
    private MutableLiveData<List<MonthYear>> listOfMonths;
    private MutableLiveData<MonthYear> lastVisibleMonthYear;
    private MutableLiveData<Integer> targetPageIndex;
    private MutableLiveData<MonthYear> targetMonthYear;
    private int currentMonthIndex;
    private boolean isFirstTime;

    public PagerViewModel() {
        listOfMonths = new MutableLiveData<>();
        lastVisibleMonthYear = new MutableLiveData<>();
        targetPageIndex = new MutableLiveData<>();
        targetMonthYear = new MutableLiveData<>();
        currentMonthIndex = 0;
        isFirstTime = true;
        getMonthsList();
<<<<<<< HEAD
<<<<<<< HEAD
        generateOccurrences();
=======
>>>>>>> 5f814f6 (checkpoint)
=======
        generateOccurrences();
>>>>>>> a1379b2 (edição e exclusão)
    }

    public MutableLiveData<List<MonthYear>> getListOfMonths() {
        return listOfMonths;
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

    public int getCurrentMonthIndex() {
        return currentMonthIndex;
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    // listener para obter uma lista de meses de acordo com os anos na database
    private void getMonthsList() {
        DatabaseReference ref = FirebaseSettings.getOccurrencesReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int firstYearOfList, lastYearOfList;

                // Cria a lista de anos
                List<Integer> yearsList = new ArrayList<>();
                for (DataSnapshot yearSnapshot : dataSnapshot.getChildren()) {
                    yearsList.add(Integer.parseInt(yearSnapshot.getKey()));
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
                        MonthYear monthYear = new MonthYear(month, year);
                        if (year == currentYear && month == currentMonth) {
                            currentMonthIndex = monthYearList.size();
                            monthYear.setCurrentMonth(true);
                        }
                        //procura o mes na database para obter os valores
                        if (dataSnapshot.hasChild(String.valueOf(year))) {
                            DataSnapshot yearSnapshot = dataSnapshot.child(String.valueOf(year));
                            if (yearSnapshot.hasChild(String.valueOf(month))) {
                                DataSnapshot monthSnapshot = yearSnapshot.child(String.valueOf(month));
<<<<<<< HEAD
<<<<<<< HEAD
                                long paidValue = 0L;
                                long unpaidValue = 0L;
                                long totalValue = 0L;
                                for (DataSnapshot occurrenceSnapshot : monthSnapshot.getChildren()) {
                                    Occurrence occurrence = occurrenceSnapshot.getValue(Occurrence.class);
                                    long value = occurrence.getValue();

                                    totalValue += value;
                                    monthYear.setHasValue(true);

                                    if (!occurrence.isPaid()) {
                                        unpaidValue += value;
                                        monthYear.setHasUnpaidValue(true);

                                        if (occurrence.getDate().isBeforeToday()) {
                                            monthYear.setHasOverdueValue(true);
                                        }
                                    } else {
                                        paidValue += value;
                                    }
                                }

=======
                                Long paidValue = 0L;
                                Long unpaidValue = 0L;
                                Long totalValue = 0L;
=======
                                long paidValue = 0L;
                                long unpaidValue = 0L;
                                long totalValue = 0L;
>>>>>>> 9fbce0d (ajustes)
                                for (DataSnapshot occurrenceSnapshot : monthSnapshot.getChildren()) {
                                    Occurrence occurrence = occurrenceSnapshot.getValue(Occurrence.class);
                                    long value = occurrence.getValue();

                                    totalValue += value;
                                    monthYear.setHasValue(true);

                                    if (!occurrence.isPaid()) {
                                        unpaidValue += value;
                                        monthYear.setHasUnpaidValue(true);

                                        if (occurrence.getDate().isBeforeToday()) {
                                            monthYear.setHasOverdueValue(true);
                                        }
                                    } else {
                                        paidValue += value;
                                    }
                                }
<<<<<<< HEAD
>>>>>>> 5f814f6 (checkpoint)
=======

>>>>>>> 9fbce0d (ajustes)
                                monthYear.setPaidValue(paidValue);
                                monthYear.setUnpaidValue(unpaidValue);
                                monthYear.setTotalValue(totalValue);
                            }
                        }
                        monthYearList.add(monthYear);
                    }
                }
                getListOfMonths().setValue(monthYearList);
<<<<<<< HEAD
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // handle error
            }
        });
    }

    private void generateOccurrences() {
        DatabaseReference ref = FirebaseSettings.getOccurrenceControllersReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot controllerSnapshot : snapshot.getChildren()) {
                    OccurrenceController occurrenceController = controllerSnapshot.getValue(OccurrenceController.class);
                    if (occurrenceController != null) {
                        occurrenceController.generateOccurrences();
                    }
                }
=======
>>>>>>> 5f814f6 (checkpoint)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // handle error
            }
        });
    }

    private void generateOccurrences() {
        DatabaseReference ref = FirebaseSettings.getOccurrenceControllersReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot controllerSnapshot : snapshot.getChildren()) {
                    OccurrenceController occurrenceController = controllerSnapshot.getValue(OccurrenceController.class);
                    if (occurrenceController != null) {
                        occurrenceController.generateOccurrences();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}