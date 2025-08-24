package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.OrderImpl;

public interface OrderRepository extends JpaRepository<OrderImpl, Integer> {

}
