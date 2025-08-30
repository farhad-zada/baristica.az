package com.shop.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shop.payment.dto.UserBalanceRequestDto;
import com.shop.payment.dto.UserBalanceResponseDto;
import com.shop.payment.entity.UserBalance;
import com.shop.payment.exception.InsufficientBalanceException;
import com.shop.payment.exception.UserBalanceNotFoundException;
import com.shop.payment.repository.UserBalanceRepository;

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

    @Transactional(propagation = Propagation.REQUIRED)
    public void deductPurchase(Integer userId, Integer amount) {
        UserBalance ub = this.repository.findById(userId).orElseThrow(() -> new RuntimeException());
        Integer newBalance = ub.getBalance() - amount;
        if (newBalance < 0) {
            throw new InsufficientBalanceException(userId);
        }
        ub.setBalance(newBalance);
    }

    @Transactional
    public UserBalanceResponseDto createUserBalance(UserBalanceRequestDto requestDto) {
        UserBalance saved = this.repository.save(new UserBalance(requestDto.getUserId(), requestDto.getBalance()));
        return new UserBalanceResponseDto(saved.getUserId(), saved.getBalance());
    }
}
