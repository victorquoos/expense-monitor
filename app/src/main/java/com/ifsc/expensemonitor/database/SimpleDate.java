package com.ifsc.expensemonitor.database;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SimpleDate {
    private int year;
    private int month;
    private int day;

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

    public SimpleDate setDate(Long millis) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(millis);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        return null;
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
    public Long getDateInMillis() {
        Calendar calendar = getCalendar();
        return calendar.getTimeInMillis();
    }

    @Exclude
    private static DateFormat getDateFormat() {
        return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    }

    @Exclude
    Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (day > maxDay) {
            day = maxDay;
        }
        calendar.set(year, month, day);
        int offset = TimeZone.getDefault().getOffset(calendar.getTimeInMillis());
        calendar.setTimeInMillis(calendar.getTimeInMillis() + offset);
        return calendar;
    }

    @Exclude
    public String getFormattedDate() {
        Calendar calendar = getCalendar();
        return getDateFormat().format(calendar.getTime());
    }

    @Exclude
    public static SimpleDate today() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Exclude
    public boolean isBeforeToday() {
        return year < today().getYear() ||
                (year == today().getYear() && month < today().getMonth()) ||
                (year == today().getYear() && month == today().getMonth() && day < today().getDay());
    }

    @Exclude
    public boolean isBefore(SimpleDate date) {
        return year < date.getYear() ||
                (year == date.getYear() && month < date.getMonth()) ||
                (year == date.getYear() && month == date.getMonth() && day < date.getDay());
    }

    @Exclude
    public boolean isAfter(SimpleDate date) {
        return year > date.getYear() ||
                (year == date.getYear() && month > date.getMonth()) ||
                (year == date.getYear() && month == date.getMonth() && day > date.getDay());
    }

    @Exclude
    public SimpleDate plusMonths(int months) {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.MONTH, months);
        return new SimpleDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Exclude
    public int compareTo(SimpleDate date) {
        if (year < date.getYear()) {
            return -1;
        } else if (year == date.getYear()) {
            if (month < date.getMonth()) {
                return -1;
            } else if (month == date.getMonth()) {
                if (day < date.getDay()) {
                    return -1;
                } else if (day == date.getDay()) {
                    return 0;
                }
            }
        }
        return 1;
    }

    public boolean isInMonth(int month, int year) {
        return this.month == month && this.year == year;
    }

    @NonNull
    public SimpleDate clone() {
        return new SimpleDate(year, month, day);
    }
}
