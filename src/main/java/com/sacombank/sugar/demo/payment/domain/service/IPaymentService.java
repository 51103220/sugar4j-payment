package com.sacombank.sugar.demo.payment.domain.service;

import com.sacombank.sugar.demo.payment.domain.Payment;

public interface IPaymentService {
    void pay(Payment order);
    void reversePayment(String orderId);
}
