package com.shop.payment.service;

import org.springframework.stereotype.Service;

import com.shop.payment.dto.UserBalanceResponseDto;
import com.shop.payment.entity.UserBalance;
import com.shop.payment.exception.InsufficientBalanceException;
import com.shop.payment.exception.UserBalanceNotFoundException;
import com.shop.payment.repository.UserBalanceRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class UserBalanceService {
    UserBalanceRepository repository;

    public UserBalanceService(UserBalanceRepository repository) {
        this.repository = repository;
    }

    public UserBalanceResponseDto getBalanceOf(Integer userId) {
        UserBalance balance = repository.findById(userId)
                .orElseThrow(() -> new UserBalanceNotFoundException(userId));
        return new UserBalanceResponseDto(balance.getUserId(), balance.getBalance());
    }

    @Transactional(value = TxType.REQUIRED)
    public void deductPurchase(Integer userId, Integer amount) {
        UserBalance ub = this.repository.findById(userId).orElseThrow(() -> new RuntimeException());
        Integer newBalance = ub.getBalance() - amount;
        if (newBalance < 0) {
            throw new InsufficientBalanceException(userId);
        }
        ub.setBalance(newBalance);
    }
}
