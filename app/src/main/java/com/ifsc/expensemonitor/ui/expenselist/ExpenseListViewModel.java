package com.ifsc.expensemonitor.ui.expenselist;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifsc.expensemonitor.data.Occurrence;
import com.ifsc.expensemonitor.data.FirebaseSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpenseListViewModel extends ViewModel {
    private ValueEventListener currentListener = null;
    private DatabaseReference currentRef = null;
    private MutableLiveData<List<Occurrence>> currentMonthOccurrences;
    private MutableLiveData<Long> paidValue;
    private MutableLiveData<Long> unpaidValue;
    private MutableLiveData<Long> totalValue;

    public ExpenseListViewModel() {
        currentMonthOccurrences = new MutableLiveData<>();
        paidValue = new MutableLiveData<>();
        unpaidValue = new MutableLiveData<>();
        totalValue = new MutableLiveData<>();
    }

    public MutableLiveData<List<Occurrence>> getCurrentMonthOccurrences() {
        return currentMonthOccurrences;
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

    public void setMonth(String year, String month) {
        if (currentListener == null) {
            currentRef = FirebaseSettings.getOccurrencesReference().child(year).child(month);

            currentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    System.out.println("onDataChange");
                    System.out.println(snapshot);
                    System.out.println(snapshot.getValue());
                    System.out.println(snapshot.getChildren());
                    System.out.println(snapshot.getChildrenCount());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            currentListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Occurrence> occurrences = new ArrayList<>();
                    Long paidValue = 0L;
                    Long unpaidValue = 0L;
                    Long totalValue = 0L;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Occurrence occurrence = snapshot.getValue(Occurrence.class);
                        occurrences.add(occurrence);
                        if (occurrence.isPaid()) {
                            paidValue += occurrence.getValue();
                        } else {
                            unpaidValue += occurrence.getValue();
                        }
                        totalValue += occurrence.getValue();
                    }

                    if (!occurrences.equals(currentMonthOccurrences.getValue())) {
                        currentMonthOccurrences.setValue(occurrences);
                    }
                    getPaidValue().setValue(paidValue);
                    getUnpaidValue().setValue(unpaidValue);
                    getTotalValue().setValue(totalValue);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // handle error
                }
            };
            currentRef.addValueEventListener(currentListener);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (currentListener != null) {
            currentRef.removeEventListener(currentListener);
        }
    }
}