package com.sacombank.sugar.demo.order.infrastructure.broker.kafka;



import com.sacombank.sugar.demo.order.domain.OrderEvent;
import com.sacombank.sugar.demo.order.domain.producer.IOrderEventProducer;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Component
public class OrderEventProducer implements IOrderEventProducer {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(OrderEventProducer.class);

    @Autowired
    private KafkaTemplate<String, OrderEvent> orderEventKafkaTemplate;

    @Value(value = "${order.topic.event}")
    private String topicName;

    @Override
    public void publish(OrderEvent orderEvent) {
        ListenableFuture<SendResult<String, OrderEvent>> future = orderEventKafkaTemplate.send(topicName, orderEvent);

            future.addCallback(new ListenableFutureCallback<SendResult<String, OrderEvent>>() {

                @Override
                public void onSuccess(SendResult<String, OrderEvent> result) {
                   logger.debug("Sent message=[" + orderEvent.getEventId() + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
                }

                @Override
                public void onFailure(Throwable ex) {
                    logger.debug("Unable to send message=[" + orderEvent.getEventId() + "] due to : " + ex.getMessage());
                }
            });
        
    }
}
