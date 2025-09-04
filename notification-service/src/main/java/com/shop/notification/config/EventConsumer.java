package com.shop.notification.config;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.common.event.OrderEvent;
import com.shop.common.event.OrderStatus;
import com.shop.common.event.PaymentEvent;
import com.shop.notification.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EventConsumer {
    private NotificationService service;
    private ObjectMapper mapper;

    public EventConsumer(NotificationService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Bean
    public Consumer<byte[]> orderConsumer() {
        return paylod -> {
            try {
                OrderEvent event = this.mapper.readValue(paylod, OrderEvent.class);
                if (OrderStatus.PENDING.equals(event.getStatus())) {
                    log.info("Skipping pending orders {}", event);
                    return;
                }
                log.info("Sending notification for {}", event);

                this.service.sendOrderMail(event);
            } catch (Exception e) {
                log.error("Failed to process event {}", e);
            }
        };
    }

    @Bean
    public Consumer<byte[]> paymentConsumer() {
        return paylod -> {
            try {
                PaymentEvent event = this.mapper.readValue(paylod, PaymentEvent.class);
                log.info("Sending notification for {}", event);

                // this.service.sendOrderMail(event);
                log.info("Got payment for {}", event);
            } catch (Exception e) {
                log.error("Failed to process event {}", e);
            }
        };
    }
}
