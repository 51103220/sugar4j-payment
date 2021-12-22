package com.sacombank.sugar.demo.order.domain.service;

import com.sacombank.sugar.demo.order.domain.Order;
import com.sacombank.sugar.demo.order.domain.OrderEvent;
import com.sacombank.sugar.demo.order.domain.producer.IOrderEventProducer;
import com.sacombank.sugar.demo.order.domain.repository.IOrderRepository;

import org.apache.logging.log4j.LogManager;

public class SugarOrderService implements IOrderService {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(SugarOrderService.class);

    private IOrderRepository orderRepository;
    private IOrderEventProducer orderEventProducer;

    public SugarOrderService(IOrderRepository orderRepository, IOrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }

    @Override
    public void createOrder(Order order) {
        logger.debug("created order: " + order);
        orderEventProducer.publish(new OrderEvent(order.getOrderId(), com.sacombank.sugar.demo.order.domain.OrderStatus.CREATED));
        
    }

    @Override
    public void deleteOrder(String orderId) {
       logger.debug("destroyed order: " + orderId);
       orderEventProducer.publish(new OrderEvent(orderId, com.sacombank.sugar.demo.order.domain.OrderStatus.CANCELED));

    }
}
