package com.ifsc.expensemonitor.data;

import com.google.firebase.database.DatabaseReference;

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
