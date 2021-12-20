package com.sacombank.sugar.demo.order.domain.service;

import com.sacombank.sugar.demo.order.domain.Order;

public interface IOrderService {
    void createOrder(Order order);
    void deleteOrder(String orderId);
}
