package com.ifsc.expensemonitor.ui.monthlist;

public class MonthYear {
    private int month;
    private int year;
    private boolean isCurrentMonth = false;

    public MonthYear(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}

