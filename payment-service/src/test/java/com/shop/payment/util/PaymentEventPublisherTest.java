package com.shop.payment.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;

import com.shop.common.event.PaymentEvent;
import com.shop.common.event.PaymentStatus;

@ExtendWith(MockitoExtension.class)
public class PaymentEventPublisherTest {

    @Mock
    StreamBridge bridge;

    PaymentEventPublisher publisher;

    PaymentEvent event;

    @BeforeEach
    void setup() {
        this.event = new PaymentEvent();
        event.setOrderId(1);
        event.setMessage("payment acquired");
        event.setStatus(PaymentStatus.SUCCESSFUL);
        publisher = new PaymentEventPublisher(bridge);
    }

    @Test
    void publish_ShouldSendMessageSuccessfully() {
        when(bridge.send(eq("paymentProducer-out-0"), any(Message.class))).thenReturn(true);
        publisher.publish(event);
        verify(bridge, times(1))
                .send(eq("paymentProducer-out-0"), any(Message.class));
    }

    @Test
    void publish_ShouldThrowExceptionWhenMessageSendingFails() {
        when(bridge.send(eq("paymentProducer-out-0"), any(Message.class)))
                .thenThrow(new RuntimeException("Kafka unavailable"));
        try {
            publisher.publish(event);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Kafka unavailable"));
        }
        verify(bridge, times(1))
                .send(eq("paymentProducer-out-0"), any(Message.class));
    }

    @Test
    void onCommitEvent_ShouldPublishSuccessEvent() {

        when(bridge.send(anyString(), any(Message.class))).thenReturn(true);
        PaymentEvent event = new PaymentEvent();
        event.setOrderId(1);

        publisher.onCommitEvent(event);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Message<PaymentEvent>> captor = ArgumentCaptor.forClass(Message.class);

        verify(bridge).send(eq("paymentProducer-out-0"), captor.capture());

        Message<PaymentEvent> message = captor.getValue();
        PaymentEvent payload = message.getPayload();
        assertEquals(PaymentStatus.SUCCESSFUL, payload.getStatus());
        assertEquals(event.getOrderId(), payload.getOrderId());
        assertEquals(event.getMessage(), payload.getMessage());

    }

    @Test
    void onCommitEvent_ShouldPublishFailEvent() {

        when(bridge.send(anyString(), any(Message.class))).thenReturn(true);
        PaymentEvent event = new PaymentEvent();
        event.setOrderId(1);

        publisher.onRollbackEvent(event);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Message<PaymentEvent>> captor = ArgumentCaptor.forClass(Message.class);

        verify(bridge).send(eq("paymentProducer-out-0"), captor.capture());

        Message<PaymentEvent> message = captor.getValue();
        PaymentEvent payload = message.getPayload();
        assertEquals(PaymentStatus.FAILED, payload.getStatus());
        assertEquals(event.getOrderId(), payload.getOrderId());
        assertEquals(event.getMessage(), payload.getMessage());

    }
}
