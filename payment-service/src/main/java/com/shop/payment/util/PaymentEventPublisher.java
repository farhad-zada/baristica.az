package com.shop.payment.util;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.shop.common.event.PaymentEvent;
import com.shop.common.event.PaymentStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentEventPublisher {

    StreamBridge streamBridge;

    public PaymentEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publish(PaymentEvent event) {
        try {

            Message<PaymentEvent> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.KEY, event.getOrderId())
                    .setHeader("kafka_messageKey", event.getOrderId())
                    .build();
            boolean sent = streamBridge.send("paymentProducer-out-0", message);
            if (sent) {
                log.info("Published {}", event);
            } else {
                log.warn("Failed to publish {}", event);
            }
        } catch (Exception e) {
            log.error("Exception while publishing event: {}", event, e);
            throw e;
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCommitEvent(PaymentEvent event) {
        this.publish(
                new PaymentEvent(
                        event.getOrderId(),
                        PaymentStatus.SUCCESSFUL,
                        event.getMessage()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onRollbackEvent(PaymentEvent event) {
        this.publish(
                new PaymentEvent(
                        event.getOrderId(),
                        PaymentStatus.FAILED,
                        event.getMessage()));
    }

}
