package com.shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entity.OrderImpl;
import com.shop.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;

    public List<OrderImpl> getOrders() {
        return repository.findAll();
    }

    public Optional<OrderImpl> getOrderById(Integer id) {
        return this.repository.findById(id);
    }
}
