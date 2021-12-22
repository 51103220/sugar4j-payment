package com.sacombank.sugar.demo.payment.domain.producer;

import com.sacombank.sugar.demo.payment.domain.OrderEvent;

public interface IOrderEventProducer {
    public void publish(OrderEvent orderEvent);
}
