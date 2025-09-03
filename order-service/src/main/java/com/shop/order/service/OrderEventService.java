package com.shop.order.service;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.shop.common.event.OrderEvent;
import com.shop.common.event.OrderStatus;
import com.shop.order.model.Order;
import com.shop.order.util.OrderMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderEventService {

    private final OrderService orderService;
    private OrderMapper orderMapper;

    StreamBridge bridge;

    public OrderEventService(StreamBridge bridge, OrderService orderService, OrderMapper orderMapper) {
        this.bridge = bridge;
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    private void publish(OrderEvent event) {
        try {
            Boolean wasSent = bridge.send("orders-out-0",
                    MessageBuilder
                            .withPayload(event)
                            .setHeader(KafkaHeaders.KEY, event.getOrderId())
                            .setHeader("kafka_messageKey", event.getOrderId())
                            .build());
            if (wasSent) {
                log.info("Published {}", event);
            } else {
                log.info("Failed to publish {}", event);
            }
        } catch (Exception e) {
            log.error("Exception while trying to publish OrderEvent (event={}) (exception=)", orderService, e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCommitEvent(Order order) {
        try {
            this.publish(this.orderMapper.toEvent(order));
        } catch (Exception e) {
            log.error("Exception while trying to map Order to OrderEvent (order={}) (exception={})", order, e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCommitEvent(OrderEvent event) {
        event.setStatus(OrderStatus.COMPLETED);
        this.publish(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onRollbackEvent(OrderEvent event) {
       event.setStatus(OrderStatus.CANCELLED);
       this.publish(event);
    }

}
