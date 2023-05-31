package com.ifsc.expensemonitor.ui.monthselector;

public class MonthYear {
    private int month;
    private int year;
    private boolean isCurrentMonth = false;

    public MonthYear(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public MonthYear(int month, int year, boolean isCurrentMonth) {
        this.month = month;
        this.year = year;
        this.isCurrentMonth = isCurrentMonth;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof MonthYear)) {
            return false;
        }

        MonthYear other = (MonthYear) obj;
        return this.month == other.month && this.year == other.year;
    }
}

