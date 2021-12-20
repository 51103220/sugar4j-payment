package com.sacombank.sugar.demo.order.infrastructure.configuration;
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

    @Value(value = "${order.topic.create}")
    private String createOrderTopic;


    @Value(value = "${order.topic.destroy}")
    private String destroyOrderTopic;
    
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
    public NewTopic topicCreateOrder() {
        return new NewTopic(createOrderTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic topicDestroyOrder() {
        return new NewTopic(destroyOrderTopic, 1, (short) 1);
    }
}
