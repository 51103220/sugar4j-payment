package com.sacombank.sugar.demo.payment.domain;

public class Payment {
    private String orderId;
    private double amount;

    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
