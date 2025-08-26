package com.shop.service;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.shop.entity.Order;

@Component
public class OrderEventService {

    StreamBridge bridge;

    public OrderEventService(StreamBridge bridge) {
        this.bridge = bridge;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishOrder(Order order) {
        Boolean wasSent = bridge.send("orders-out-0",
                MessageBuilder
                        .withPayload(order)
                        .setHeader(KafkaHeaders.KEY, order.getId())
                        .setHeader("kafka_messageKey", order.getId())
                        .build());
        if (wasSent) {
            System.out.println("Data was sent successfully!");
        }
    }

}
