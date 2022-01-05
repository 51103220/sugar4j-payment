package com.sacombank.sugar.demo.payment.infrastructure.configuration;

import java.util.List;

import com.sacombank.sugar.demo.payment.domain.OrderEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value(value = "${redis.master.name}")
    private String masterName;
    @Value(value = "#{${redis.sentinels}}")
    private List<String> sentinels;

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {

        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(masterName);

        for (String address : sentinels) {
            String[] parts = address.split(":");
            String endpoint = parts[0];
            Integer port = Integer.parseInt(parts[1]);

            sentinelConfig = sentinelConfig.sentinel(endpoint, port);
        }

        return new LettuceConnectionFactory(sentinelConfig);
    }
    
    @Bean
    public ReactiveRedisTemplate<String, OrderEvent> orderEventCache() {
        Jackson2JsonRedisSerializer<OrderEvent> serializer = new Jackson2JsonRedisSerializer<>(OrderEvent.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, OrderEvent> builder = RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, OrderEvent> context = builder.value(serializer)
            .build();
        return new ReactiveRedisTemplate<String, OrderEvent>((ReactiveRedisConnectionFactory) lettuceConnectionFactory(), context);
    }
}
