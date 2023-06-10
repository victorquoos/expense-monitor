package com.ifsc.expensemonitor.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class OccurrenceService {
    public static void save(Occurrence occurrence) {
        DatabaseReference occurrencesRef = FirebaseSettings.getOccurrencesReference()
                .child(String.valueOf(occurrence.getDate().getYear()))
                .child(String.valueOf(occurrence.getDate().getMonth()));
        String id = occurrencesRef.push().getKey();
        occurrence.setId(id);
        occurrencesRef.child(id).setValue(occurrence);
    }

    public static void update(Occurrence occurrence) {
        // atualiza a ocorrência
        DatabaseReference occurrenceRef = FirebaseSettings.getOccurrencesReference()
                .child(String.valueOf(occurrence.getDate().getYear()))
                .child(String.valueOf(occurrence.getDate().getMonth()))
                .child(occurrence.getId());

        // verificar se a referencia possui algum valor
        occurrenceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // se não existir, procura pela referencia e deleta
                    DatabaseReference ref = FirebaseSettings.getOccurrencesReference();
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot yearSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                                    for (DataSnapshot occurrenceSnapshot : monthSnapshot.getChildren()) {
                                        if (Objects.equals(occurrenceSnapshot.getKey(), occurrence.getId())) {
                                            occurrenceSnapshot.getRef().removeValue();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Tratar qualquer erro que ocorra durante a consulta
            }
        });

        occurrenceRef.setValue(occurrence);
    }
}
