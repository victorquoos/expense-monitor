package com.ifsc.expensemonitor.expenselist;

import java.util.Date;

public class ExpenseCard {
    private String name;
    private double value;
    private Date date;
    private boolean isPaid;

    public ExpenseCard(String name, double value, Date date, boolean isPaid) {
        this.name = name;
        this.value = value;
        this.date = date;
        this.isPaid = isPaid;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }

    public boolean isPaid() {
        return isPaid;
    }
}
