<<<<<<< HEAD:app/src/main/java/com/ifsc/expensemonitor/data/Occurrence.java
package com.ifsc.expensemonitor.data;
=======
package com.ifsc.expensemonitor.database;

import com.google.firebase.database.Exclude;
>>>>>>> 5f814f6 (checkpoint):app/src/main/java/com/ifsc/expensemonitor/database/Occurrence.java

public class Occurrence {
    private String id;
    private String groupId;
    private int index;
    private String name;
    private Long value;
    private SimpleDate date;
    private String description;
    private boolean paid;

    public Occurrence() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public SimpleDate getDate() {
        return date;
    }

    public void setDate(SimpleDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
