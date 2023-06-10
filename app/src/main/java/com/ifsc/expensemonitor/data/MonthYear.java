package com.ifsc.expensemonitor.data;

import java.util.Calendar;

public class MonthYear {

    private int month;
    private int year;
    private boolean isCurrentMonth = false;
    private Long paidValue = 0L;
    private Long unpaidValue = 0L;
    private Long totalValue = 0L;
    private boolean hasValue = false;
    private boolean hasUnpaidValue = false;
    private boolean hasOverdueValue = false;

    public MonthYear(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public MonthYear(int month, int year, boolean isCurrentMonth) {
        this.month = month;
        this.year = year;
        this.isCurrentMonth = isCurrentMonth;
    }

    public MonthYear(int month, int year, boolean isCurrentMonth, Long paidValue, Long unpaidValue, Long totalValue) {
        this.month = month;
        this.year = year;
        this.isCurrentMonth = isCurrentMonth;
        this.paidValue = paidValue;
        this.unpaidValue = unpaidValue;
        this.totalValue = totalValue;
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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public Long getPaidValue() {
        return paidValue;
    }

    public void setPaidValue(Long paidValue) {
        this.paidValue = paidValue;
    }

    public Long getUnpaidValue() {
        return unpaidValue;
    }

    public void setUnpaidValue(Long unpaidValue) {
        this.unpaidValue = unpaidValue;
    }

    public Long getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Long totalValue) {
        this.totalValue = totalValue;
    }

    public boolean hasValue() {
        return hasValue;
    }

    public void setHasValue(boolean hasValue) {
        this.hasValue = hasValue;
    }

    public boolean hasUnpaidValue() {
        return hasUnpaidValue;
    }

    public void setHasUnpaidValue(boolean hasUnpaidValue) {
        this.hasUnpaidValue = hasUnpaidValue;
    }

    public boolean hasOverdueValue() {
        return hasOverdueValue;
    }

    public void setHasOverdueValue(boolean hasOverdueValue) {
        this.hasOverdueValue = hasOverdueValue;
    }

    public boolean isBefore(MonthYear other) {
        if (this.year < other.year) {
            return true;
        } else if (this.year == other.year) {
            return this.month < other.month;
        } else {
            return false;
        }
    }

    public static MonthYear today() {
        Calendar calendar = Calendar.getInstance();
        return new MonthYear(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }
}

