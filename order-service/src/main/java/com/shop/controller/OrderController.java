package com.shop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.entity.OrderImpl;
import com.shop.service.OrderService;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService service;

    @GetMapping("")
    public ResponseEntity<List<OrderImpl>> getOrders() {
        return ResponseEntity.ok().body(service.getOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderImpl> getOrderById(@PathVariable @NonNull @Min(0) Integer id) {
        Optional<OrderImpl> optionalOrder = this.service.getOrderById(id);
        return optionalOrder
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity
                        .notFound()
                        .build());
    }

    @GetMapping("/load")
    @ResponseBody
    public String testLoad() {
        return "ok";
    }

}
