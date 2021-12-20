package com.sacombank.sugar.demo.order.application;

import com.sacombank.sugar.demo.order.domain.Order;
import com.sacombank.sugar.demo.order.domain.service.IOrderService;

import org.apache.logging.log4j.LogManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderKafkaConsumer {
    private final IOrderService orderService;
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(OrderKafkaConsumer.class);

    public OrderKafkaConsumer(IOrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "${order.topic.create}", containerFactory = "orderKafkaListenerContainerFactory")
    public void createOrderListener(Order order) {
        logger.debug("received order create request: " + order);
        orderService.createOrder(order);
    }

    @KafkaListener(topics = "${order.topic.destroy}", containerFactory = "orderKafkaListenerContainerFactory")
    public void destroyOrderListener(Order order) {
        logger.debug("received order delete request: " + order);
        orderService.deleteOrder(order.getOrderId());
    }
}
