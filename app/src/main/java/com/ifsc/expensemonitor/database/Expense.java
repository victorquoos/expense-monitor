package com.ifsc.expensemonitor.database;

import com.google.firebase.database.Exclude;

import java.time.LocalDate;
import java.util.Calendar;

public class Expense { //TODO: check if can use Calendar or Date instead of year, month and day
    private String key;
    private String name;
    private double value; //TODO: convert to int or long with cents
    private SimpleDate date;
    private boolean paid = false;
    private String description;

    public Expense() {
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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
