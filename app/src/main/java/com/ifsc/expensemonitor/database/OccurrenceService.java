package com.ifsc.expensemonitor.database;

import com.google.firebase.database.DatabaseReference;

public class OccurrenceService {
    public static void save(Occurrence occurrence) {
        DatabaseReference occurrencesRef = FirebaseSettings.getOccurrencesReference()
                .child(String.valueOf(occurrence.getDate().getYear()))
                .child(String.valueOf(occurrence.getDate().getMonth()));
        String id = occurrencesRef.push().getKey();
        occurrence.setId(id);
        occurrencesRef.child(id).setValue(occurrence);
    }
}
