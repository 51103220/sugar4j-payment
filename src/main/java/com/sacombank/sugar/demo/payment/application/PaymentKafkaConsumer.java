package com.sacombank.sugar.demo.payment.application;

import com.sacombank.sugar.demo.payment.domain.Payment;
import com.sacombank.sugar.demo.payment.domain.service.IPaymentService;

import org.apache.logging.log4j.LogManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentKafkaConsumer {
    private final IPaymentService paymentService;
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PaymentKafkaConsumer.class);

    public PaymentKafkaConsumer(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "${payment.topic.pay}", containerFactory = "paymentKafkaListenerContainerFactory")
    public void createOrderListener(Payment payment) throws Exception {
        if (Math.random() >= 0.5){
            logger.debug("order orderId={} error msg=UnableToProcessPayment", payment.getOrderId());
            throw new Exception(String.format("orderId=%s - Payment Error", payment.getOrderId()));
        }

        logger.debug("order orderId={} received payment request", payment.getOrderId());
        paymentService.pay(payment);
    }

    @KafkaListener(topics = "${payment.topic.reverse}", containerFactory = "paymentKafkaListenerContainerFactory")
    public void destroyOrderListener(Payment payment) {
        logger.debug("order orderId={} received reverse request", payment.getOrderId());
        paymentService.reversePayment(payment.getOrderId());
    }
}
