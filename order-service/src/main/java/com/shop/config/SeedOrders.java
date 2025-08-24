package com.shop.config;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.shop.entity.OrderImpl;
import com.shop.repository.OrderRepository;

import jakarta.annotation.PostConstruct;

@Configuration
public class SeedOrders {

    @Autowired
    OrderRepository repository;

    @PostConstruct
    public void seedOrders() {
        if (repository.findAll().isEmpty()) {
            repository.saveAll(Stream.of(
                    new OrderImpl(null, 10, 100, LocalDateTime.now()),
                    new OrderImpl(null, 11, 50, LocalDateTime.now()),
                    new OrderImpl(null, 12, 400, LocalDateTime.now())).toList());
        }
    }
}
