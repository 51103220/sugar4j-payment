package com.sacombank.sugar.demo.payment.domain.service;

import java.time.Duration;

import com.sacombank.sugar.demo.payment.domain.OrderEvent;
import com.sacombank.sugar.demo.payment.domain.Payment;
import com.sacombank.sugar.demo.payment.domain.producer.IOrderEventProducer;
import com.sacombank.sugar.demo.payment.domain.repository.IPaymentRepository;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Metrics;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;

public class PaymentService implements IPaymentService {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PaymentService.class);

    private IPaymentRepository paymentRepository;
    private IOrderEventProducer orderEventProducer;

    @Autowired
    private ReactiveRedisTemplate<String, OrderEvent> orderEventCache;

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

        if (Math.random() >= 0.5) {
            logger.debug("order orderId={} payment successfully", order.getOrderId());
            OrderEvent event = new OrderEvent(order.getOrderId(),
                    com.sacombank.sugar.demo.payment.domain.OrderStatus.PAID);
            orderEventCache.opsForValue()
                    .set(String.format("order:event:%s", order.getOrderId()), event, Duration.ofMinutes(5))
                    .subscribe(
                            val -> {
                                logger.debug("orderId={} write message success: {}", order.getOrderId(), val);
                            },
                            err -> {
                                logger.error("orderId={} can not write message to redis", order.getOrderId());
                            },
                            () -> {
                                logger.debug("mono consumed");
                            });
            orderEventProducer.publish(event);
        } else {
            logger.debug("order orderId={} payment failed", order.getOrderId());

            Metrics.counter("payment.failed.total", "type", "payment").increment();
            OrderEvent event = new OrderEvent(order.getOrderId(),
                    com.sacombank.sugar.demo.payment.domain.OrderStatus.FAILED_PAYMENT);
            orderEventCache.opsForValue()
                    .set(String.format("order:event:%s", order.getOrderId()), event, Duration.ofMinutes(5))
                    .subscribe(
                            val -> {
                                logger.debug("orderId={} write message to {}", order.getOrderId(), val);
                            },
                            err -> {
                                logger.error("orderId={} can not write message to redis", order.getOrderId());
                            },
                            () -> {
                                logger.debug("mono consumed");
                            });
            orderEventProducer.publish(event);
        }

    }

    @Timed(value = "payment.reverse.time", description = "Time taken to execute order payment reverse")
    @WithSpan
    @Override
    public void reversePayment(String orderId) {
        logger.debug("order orderId={} reversed payment" + orderId);
        orderEventProducer
                .publish(new OrderEvent(orderId, com.sacombank.sugar.demo.payment.domain.OrderStatus.REVERSED_PAYMENT));

    }
}
