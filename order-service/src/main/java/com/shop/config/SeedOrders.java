package com.shop.config;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.shop.entity.Order;
import com.shop.common.event.OrderStatus;
import com.shop.repository.OrderRepository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Configuration
@Profile("dev")
public class SeedOrders {

    @Autowired
    OrderRepository repository;

    @PostConstruct
    public void seedOrders() {
        if (repository.findAll().isEmpty()) {
            repository.saveAll(Stream.of(
                    new Order(null, 10, 90, 100, OrderStatus.CANCELLED, LocalDateTime.now()),
                    new Order(null, 11, 91, 50, OrderStatus.CANCELLED, LocalDateTime.now()),
                    new Order(null, 12, 93, 400, OrderStatus.CANCELLED,LocalDateTime.now())).toList());
        }
    }

    @PreDestroy
    public void freshOrders() {
        // repository.deleteAll();
    }
}
