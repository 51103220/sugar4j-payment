package com.sacombank.sugar.demo.order.domain.producer;

import com.sacombank.sugar.demo.order.domain.OrderEvent;

public interface IOrderEventProducer {
    public void publish(OrderEvent orderEvent);
}
