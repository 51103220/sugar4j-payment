package com.sacombank.sugar.demo.payment.domain.service;

import com.sacombank.sugar.demo.payment.domain.OrderEvent;
import com.sacombank.sugar.demo.payment.domain.Payment;
import com.sacombank.sugar.demo.payment.domain.producer.IOrderEventProducer;
import com.sacombank.sugar.demo.payment.domain.repository.IPaymentRepository;

import org.apache.logging.log4j.LogManager;

import io.micrometer.core.annotation.Timed;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;

public class PaymentService implements IPaymentService {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PaymentService.class);

    private IPaymentRepository paymentRepository;
    private IOrderEventProducer orderEventProducer;

    public PaymentService(IPaymentRepository paymentRepository, IOrderEventProducer orderEventProducer) {
        this.paymentRepository = paymentRepository;
        this.orderEventProducer = orderEventProducer;
    }
    
    @Timed(value = "payment.pay.time", description = "Time taken to execute order pay")
    @WithSpan
    @Override
    public void pay(Payment order) {
        Span span = Span.current();
        span.setAttribute("orderId", order.getOrderId());

        if ( Math.random() >= 0.5) {
            logger.debug("order orderId={} payment successfully", order.getOrderId());
            orderEventProducer.publish(new OrderEvent(order.getOrderId(), com.sacombank.sugar.demo.payment.domain.OrderStatus.PAID));
        }
        else {
            logger.debug("order orderId={} payment failed", order.getOrderId());
            orderEventProducer.publish(new OrderEvent(order.getOrderId(), com.sacombank.sugar.demo.payment.domain.OrderStatus.FAILED_PAYMENT));
        }
        
    }
    
    @Timed(value = "payment.reverse.time", description = "Time taken to execute order payment reverse")
    @WithSpan
    @Override
    public void reversePayment(String orderId) {
       logger.debug("order orderId={} reversed payment" + orderId);
       orderEventProducer.publish(new OrderEvent(orderId, com.sacombank.sugar.demo.payment.domain.OrderStatus.REVERSED_PAYMENT));

    }
}
