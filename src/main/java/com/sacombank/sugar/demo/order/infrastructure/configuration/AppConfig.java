package com.sacombank.sugar.demo.order.infrastructure.configuration;

import com.sacombank.sugar.demo.order.OrderApplication;
import com.sacombank.sugar.demo.order.domain.producer.IOrderEventProducer;
import com.sacombank.sugar.demo.order.domain.repository.IOrderRepository;
import com.sacombank.sugar.demo.order.domain.service.IOrderService;
import com.sacombank.sugar.demo.order.domain.service.SugarOrderService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = OrderApplication.class)
public class AppConfig {
    @Bean
    IOrderService orderService(final IOrderRepository orderRepository, final IOrderEventProducer producer) {
        return new SugarOrderService(orderRepository, producer);
    }
}
