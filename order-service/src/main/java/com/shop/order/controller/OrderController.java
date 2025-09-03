package com.shop.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.order.dto.OrderRequestDto;
import com.shop.order.dto.OrderResponseDto;
import com.shop.order.service.OrderService;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/order")
public class OrderController {

    OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<OrderResponseDto>> getOrders() {
        return ResponseEntity.ok().body(service.getOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable @NonNull @Min(0) Integer id) {
        return ResponseEntity.ok().body(this.service.getOrderById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDto dto) {
        OrderResponseDto created = this.service.createOrder(dto);
        URI location = URI.create("/order/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

}
