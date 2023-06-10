package com.ifsc.expensemonitor.data;

import java.text.NumberFormat;

public final class MoneyValue {

    private MoneyValue() {

    }

    public static String format(Long value) {
        Double centsValue = value.doubleValue() / 100;
        return NumberFormat.getCurrencyInstance().format(centsValue);
    }

    public static String format(Integer value) {
        Double centsValue = value.doubleValue() / 100;
        return NumberFormat.getCurrencyInstance().format(centsValue);
    }

    public static String format(String value) {
        Double centsValue = Double.parseDouble(value) / 100;
        return NumberFormat.getCurrencyInstance().format(centsValue);
    }
}
