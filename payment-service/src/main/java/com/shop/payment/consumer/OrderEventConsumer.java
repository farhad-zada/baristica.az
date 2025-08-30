package com.shop.payment.consumer;

import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.common.event.OrderEvent;
import com.shop.common.event.OrderStatus;
import com.shop.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderEventConsumer {

    ObjectMapper objectMapper;
    PaymentService paymentService;

    public OrderEventConsumer(PaymentService paymentService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public Consumer<byte[]> orderConsumer() {
        return paylod -> {
            try {
                OrderEvent orderEvent = objectMapper.readValue(paylod, OrderEvent.class);
                if (OrderStatus.PENDING.equals(orderEvent.getStatus())) {
                    this.paymentService.paymentProcessor(orderEvent);
                }
            } catch (Exception e) {
                log.error("Failed to process order event: ", e);
            }
        };
    }
}
