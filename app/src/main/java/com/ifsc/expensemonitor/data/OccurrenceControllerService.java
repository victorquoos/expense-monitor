<<<<<<< HEAD:app/src/main/java/com/ifsc/expensemonitor/data/OccurrenceControllerService.java
package com.ifsc.expensemonitor.data;
=======
package com.ifsc.expensemonitor.database;
>>>>>>> 5f814f6 (checkpoint):app/src/main/java/com/ifsc/expensemonitor/database/OccurrenceControllerService.java

import com.google.firebase.database.DatabaseReference;

public class OccurrenceControllerService {
    public static void save(OccurrenceController controller) {
        DatabaseReference controllersRef = FirebaseSettings.getOccurrenceControllersReference();
        String groupId = controllersRef.push().getKey();
        controller.setGroupId(groupId);
        controllersRef.child(groupId).setValue(controller);
        controller.generateOccurrences();
    }
<<<<<<< HEAD:app/src/main/java/com/ifsc/expensemonitor/data/OccurrenceControllerService.java

    public static void update(OccurrenceController controller) {
        DatabaseReference controllersRef = FirebaseSettings.getOccurrenceControllersReference();
        controllersRef.child(controller.getGroupId()).setValue(controller);
        controller.generateOccurrences();
    }
=======
>>>>>>> 5f814f6 (checkpoint):app/src/main/java/com/ifsc/expensemonitor/database/OccurrenceControllerService.java
}
