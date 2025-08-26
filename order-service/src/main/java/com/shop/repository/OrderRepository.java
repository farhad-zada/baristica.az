package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
