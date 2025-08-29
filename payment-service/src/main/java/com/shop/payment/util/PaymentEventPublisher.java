package com.shop.payment.util;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.shop.common.event.PaymentEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentEventPublisher {

    StreamBridge streamBridge;

    public PaymentEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(PaymentEvent event) {
        Message<PaymentEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.KEY, event.getOrderId())
                .setHeader("kafka_messageKey", event.getOrderId())
                .build();
        streamBridge.send("paymentProducer-out-0", message);
        log.info("Published PaymentEvent AFTER COMMIT: {}", event);
    }

}
