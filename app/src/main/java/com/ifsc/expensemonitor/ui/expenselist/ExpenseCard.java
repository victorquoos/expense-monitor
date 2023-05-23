package com.ifsc.expensemonitor.ui.expenselist;

import java.util.Date;

public class ExpenseCard {
    private String name;
    private double value;
    private Date date;
    private boolean isPaid;

    public ExpenseCard() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
