package com.shop.payment.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.common.event.OrderEvent;
import com.shop.common.event.OrderStatus;
import com.shop.payment.service.PaymentService;

@ExtendWith(MockitoExtension.class)
public class OrderEventConsumerTest {
    @Mock
    ObjectMapper mapper;
    @Mock
    PaymentService service;

    @InjectMocks
    OrderEventConsumer consumer;

    @Test
    void orderConsumer_ShouldCallPaymentProcessorWhenOrderPENDING() throws Exception {
        OrderEvent event = new OrderEvent();
        event.setUserId(1);
        event.setCost(100);
        event.setStatus(OrderStatus.PENDING);

        when(mapper.readValue(any(byte[].class), eq(OrderEvent.class))).thenReturn(event);
        
        Consumer<byte[]> func = consumer.orderConsumer();

        func.accept("dummy".getBytes());
        verify(service, times(1)).paymentProcessor(event);
    }

     @Test
    void orderConsumer_ShouldNotCallPaymentProcessorWhenOrderNotPENDING() throws Exception {
        OrderEvent event = new OrderEvent();
        event.setUserId(1);
        event.setCost(100);
        event.setStatus(OrderStatus.CANCELLED);

        when(mapper.readValue(any(byte[].class), eq(OrderEvent.class))).thenReturn(event);
        
        Consumer<byte[]> func = consumer.orderConsumer();

        func.accept("dummy".getBytes());
        verify(service, never()).paymentProcessor(event);
    }

     @Test
    void orderConsumer_ShouldLogErrorOnException() throws Exception {

        when(mapper.readValue(any(byte[].class), eq(OrderEvent.class))).thenThrow(new RuntimeException("smth went wrng"));
        
        Consumer<byte[]> func = consumer.orderConsumer();

        func.accept("dummy".getBytes());
        verify(service, never()).paymentProcessor(any());
    }

}
