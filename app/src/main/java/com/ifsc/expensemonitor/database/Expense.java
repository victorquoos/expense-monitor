package com.ifsc.expensemonitor.database;

import com.google.firebase.database.Exclude;

import java.util.Calendar;

public class Expense { //TODO: check if can use Calendar or Date instead of year, month and day
    private String key;
    private String name;
    private double value; //TODO: convert to int or long with cents
    private int year;
    private int month;
    private int day;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setCalendar(Calendar calendar) {
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Exclude
    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
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
