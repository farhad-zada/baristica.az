package com.shop.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.payment.entity.UserBalance;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Integer> {

}
