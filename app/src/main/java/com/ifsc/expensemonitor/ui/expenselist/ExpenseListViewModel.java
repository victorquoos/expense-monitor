package com.ifsc.expensemonitor.ui.expenselist;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.database.Expense;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.database.SimpleDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpenseListViewModel extends ViewModel {
    private ValueEventListener currentListener = null;
    private DatabaseReference currentRef = null;
    private MutableLiveData<Integer> month;
    private MutableLiveData<Integer> year;
    private MutableLiveData<Long> paidValue;
    private MutableLiveData<Long> unpaidValue;
    private MutableLiveData<Long> totalValue;
    private MutableLiveData<List<Expense>> currentMonthExpenses;

    public ExpenseListViewModel() {
        month = new MutableLiveData<>();
        year = new MutableLiveData<>();
        paidValue = new MutableLiveData<>();
        unpaidValue = new MutableLiveData<>();
        totalValue = new MutableLiveData<>();
        currentMonthExpenses = new MutableLiveData<>(new ArrayList<>()); //TODO: Por que esse arraylist no final?

        int month = SimpleDate.getCurrentDate().getMonth();
        int year = SimpleDate.getCurrentDate().getYear();
        loadExpensesForMonth(month, year);
    }

    public MutableLiveData<Integer> getMonth() {
        return month;
    }

    public MutableLiveData<Integer> getYear() {
        return year;
    }

    public MutableLiveData<Long> getPaidValue() {
        return paidValue;
    }

    public MutableLiveData<Long> getUnpaidValue() {
        return unpaidValue;
    }

    public MutableLiveData<Long> getTotalValue() {
        return totalValue;
    }

    public MutableLiveData<List<Expense>> getCurrentMonthExpenses() {
        return currentMonthExpenses;
    }

    public void goToNextMonth() {
        int month = this.month.getValue();
        int year = this.year.getValue();
        if (month == 11) {
            month = 0;
            year++;
        } else {
            month++;
        }
        loadExpensesForMonth(month, year);
    }

    public void goToPreviousMonth() {
        int month = this.month.getValue();
        int year = this.year.getValue();
        if (month == 0) {
            month = 11;
            year--;
        } else {
            month--;
        }
        loadExpensesForMonth(month, year);
    }

    public void loadExpensesForMonth(int month, int year) {
        this.month.setValue(month);
        this.year.setValue(year);

        if (currentListener != null && currentRef != null) {
            currentRef.removeEventListener(currentListener);
        }
        DatabaseReference monthReference = FirebaseSettings.getMonthReference(year, month);
        currentRef = monthReference;

        currentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Expense> expenses = new ArrayList<>();
                Long totalPaid = 0L;
                Long totalPending = 0L;
                for (DataSnapshot expenseData : snapshot.getChildren()) {
                    Expense expense = expenseData.getValue(Expense.class);
                    expense.setKey(expenseData.getKey());
                    expenses.add(expense);
                    if (expense.isPaid()) {
                        totalPaid += expense.getValue();
                    } else {
                        totalPending += expense.getValue();
                    }
                }

                Collections.sort(expenses, (o1, o2) -> o1.getDate().compareTo(o2.getDate())); //TODO: create other sorting methods

                currentMonthExpenses.setValue(expenses);
                paidValue.setValue(totalPaid);
                unpaidValue.setValue(totalPending);
                totalValue.setValue(totalPaid + totalPending);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        monthReference.addValueEventListener(currentListener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (currentListener != null && currentRef != null) {
            currentRef.removeEventListener(currentListener);
        }
    }


}