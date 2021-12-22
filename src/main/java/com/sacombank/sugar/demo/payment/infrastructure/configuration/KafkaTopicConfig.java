package com.sacombank.sugar.demo.payment.infrastructure.configuration;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${payment.topic.pay}")
    private String createPaymentTopic;


    @Value(value = "${payment.topic.reverse}")
    private String reversePaymentTopic;
    
    @Value(value = "${kafka.jaas.config}")
    private String jaasConfig;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put("sasl.mechanism", "PLAIN");
        configs.put("sasl.jaas.config", jaasConfig);
        configs.put("security.protocol", "SASL_SSL");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicCreatePayment() {
        return new NewTopic(createPaymentTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic topicReversePaymeNewTopic() {
        return new NewTopic(reversePaymentTopic, 1, (short) 1);
    }
}
