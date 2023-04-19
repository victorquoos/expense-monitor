package com.ifsc.expensemonitor.database;

import com.google.firebase.database.Exclude;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SimpleDate {
    private int year;
    private int month;
    private int day;
    private static DateFormat dateFormat;
    private static SimpleDate today;

    public SimpleDate() {

    }

    public SimpleDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setDate(Long millis) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(millis);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
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

    @Exclude
    private static DateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        }
        return dateFormat;
    }

    @Exclude
    private static SimpleDate getToday(){
        if(today == null){
            today = new SimpleDate();
            today.setDate(Calendar.getInstance().getTimeInMillis());
        }
        return today;
    }

    @Exclude Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }

    @Exclude
    public String getFormattedDate() {
        Calendar calendar = getCalendar();
        return getDateFormat().format(calendar.getTime());
    }

    @Exclude
    public static SimpleDate getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Exclude
    public boolean isBeforeToday(){
        if (year < getToday().getYear()) {
            return true;
        } else if (year == getToday().getYear()) {
            if (month < getToday().getMonth()) {
                return true;
            } else if (month == getToday().getMonth()) {
                return day < getToday().getDay();
            }
        }
        return false;
    }
}
