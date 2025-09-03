package com.shop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
