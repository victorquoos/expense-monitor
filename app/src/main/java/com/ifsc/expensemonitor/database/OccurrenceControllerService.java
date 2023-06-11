package com.ifsc.expensemonitor.database;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class OccurrenceControllerService {
    public static void save(OccurrenceController controller) {
        DatabaseReference controllersRef = FirebaseSettings.getOccurrenceControllersReference();
        String groupId = controllersRef.push().getKey();
        controller.setGroupId(groupId);
        controllersRef.child(groupId).setValue(controller);
        controller.generateOccurrences();
    }

    public static void update(OccurrenceController controller) {
        DatabaseReference controllersRef = FirebaseSettings.getOccurrenceControllersReference();
        controllersRef.child(controller.getGroupId()).setValue(controller);
        controller.generateOccurrences();
    }
}
