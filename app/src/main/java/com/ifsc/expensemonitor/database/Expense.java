package com.ifsc.expensemonitor.database;

import com.google.firebase.database.Exclude;

import java.text.NumberFormat;

public class Expense {

    private String key;
    private String name;
    private Long value;
    private SimpleDate date;
    private boolean paid = false;
    private String description;

    public Expense() {
    }

    public Expense(String name, Long value, SimpleDate date, String description) {
        this.name = name;
        this.value = value;
        this.date = date;
        this.description = description;
    }

    public void save() {
        FirebaseSettings.getExpensesReference()
                .push()
                .setValue(this);
    }

    public void delete() {
        FirebaseSettings.getExpensesReference()
                .child(getKey())
                .removeValue();
    }

    public void update() {
        FirebaseSettings.getExpensesReference()
                .child(getKey())
                .setValue(this);
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
