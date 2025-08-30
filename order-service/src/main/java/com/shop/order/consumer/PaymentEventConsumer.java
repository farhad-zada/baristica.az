package com.shop.order.consumer;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.common.event.PaymentEvent;
import com.shop.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentEventConsumer {

    OrderService orderService;
    ObjectMapper objectMapper;

    public PaymentEventConsumer(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public Consumer<byte[]> paymentConsumer() {
        return payload -> {
            try {
                PaymentEvent event = objectMapper.readValue(payload, PaymentEvent.class);
                this.orderService.paymentProcessed(event);
            } catch (Exception e) {
                log.error("Failed to map (payload={}) to PaymentEvent class. Error: {}", payload, e);
            }
        };
    }

}
