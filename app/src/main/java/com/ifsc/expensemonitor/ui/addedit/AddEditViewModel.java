package com.ifsc.expensemonitor.ui.addedit;

import static com.ifsc.expensemonitor.database.FirebaseSettings.getMonthReference;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.database.Expense;
import com.ifsc.expensemonitor.database.FirebaseSettings;

public class AddEditViewModel extends ViewModel {
    private MutableLiveData<Expense> expense;

    public AddEditViewModel() {
        expense = new MutableLiveData<>();
    }

    public LiveData<Expense> getExpense() {
        return expense;
    }

    public void loadExpenseData(int month, int year, String key) {
        DatabaseReference expenseReference = FirebaseSettings.getMonthReference(year, month).child(key);
        expenseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expense.setValue(snapshot.getValue(Expense.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AddEditViewModel", "Erro ao obter despesa", error.toException());
            }
        });
    }
}