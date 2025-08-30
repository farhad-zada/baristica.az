package com.shop.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.shop.payment.dto.UserBalanceRequestDto;
import com.shop.payment.dto.UserBalanceResponseDto;
import com.shop.payment.entity.UserBalance;
import com.shop.payment.repository.UserBalanceRepository;

@ExtendWith(MockitoExtension.class)
public class UserBalanceServiceTest {

    @Mock
    UserBalanceRepository repository;

    @InjectMocks
    private UserBalanceService userBalanceService;

    @Test
    void shouldReturnUserBalances() {
        Mockito.when(repository.findById(10))
                .thenReturn(Optional.of(new UserBalance(10, 200)));

        UserBalanceResponseDto response = this.userBalanceService.getBalanceOf(10);
        assertEquals(response.getBalance(), 200);
        assertEquals(response.getUserId(), 10);
    }

    @Test
    void shouldCreateUserBalance() {
        UserBalance userBalance = new UserBalance(1, 2000);
        Mockito.when(repository.save(userBalance)).thenReturn(new UserBalance(
                userBalance.getUserId(),
                userBalance.getBalance()));

        UserBalanceResponseDto response = this.userBalanceService
                .createUserBalance(new UserBalanceRequestDto(userBalance.getUserId(), userBalance.getBalance()));
        assertEquals(userBalance.getUserId(), response.getUserId());
        assertEquals(userBalance.getBalance(), response.getBalance());

    }

        @Test
    void shouldFailOnCreateUserBalance() {
        UserBalance userBalance = new UserBalance(1, 2000);
        Mockito.when(repository.save(userBalance)).thenReturn(new UserBalance(
                userBalance.getUserId(),
                userBalance.getBalance()));

        UserBalanceResponseDto response = this.userBalanceService
                .createUserBalance(new UserBalanceRequestDto(userBalance.getUserId(), userBalance.getBalance()));
        assertEquals(userBalance.getUserId(), response.getUserId());
        assertEquals(userBalance.getBalance(), response.getBalance());

    }

}
