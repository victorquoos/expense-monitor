package com.ifsc.expensemonitor.database;

import com.google.firebase.database.DatabaseReference;

public class OccurrenceControllerService {
    public static void save(OccurrenceController controller) {
        DatabaseReference controllersRef = FirebaseSettings.getOccurrenceControllersReference();
        String groupId = controllersRef.push().getKey();
        controller.setGroupId(groupId);
        controllersRef.child(groupId).setValue(controller);
        controller.generateOccurrences();
    }
}
