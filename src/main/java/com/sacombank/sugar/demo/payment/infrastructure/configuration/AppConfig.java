package com.sacombank.sugar.demo.payment.infrastructure.configuration;

import com.sacombank.sugar.demo.payment.PaymentApplication;
import com.sacombank.sugar.demo.payment.domain.producer.IOrderEventProducer;
import com.sacombank.sugar.demo.payment.domain.repository.IPaymentRepository;
import com.sacombank.sugar.demo.payment.domain.service.IPaymentService;
import com.sacombank.sugar.demo.payment.domain.service.PaymentService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PaymentApplication.class)
public class AppConfig {
    @Bean
    IPaymentService orderService(final IPaymentRepository orderRepository, final IOrderEventProducer producer) {
        return new PaymentService(orderRepository, producer);
    }
}
