package com.shop.payment.consumer;

import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.payment.entity.Order;
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
                Order order = objectMapper.readValue(paylod, Order.class);
                this.paymentService.paymentProcessor(order);
            } catch (Exception e) {
                log.error("Failed to process order event: ", e);
            }
        };
    }
}
