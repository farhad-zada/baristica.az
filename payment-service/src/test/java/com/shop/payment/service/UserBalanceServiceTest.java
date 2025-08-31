package com.shop.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

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
import com.shop.payment.exception.InsufficientBalanceException;
import com.shop.payment.exception.UserBalanceNotFoundException;
import com.shop.payment.repository.UserBalanceRepository;

@ExtendWith(MockitoExtension.class)
public class UserBalanceServiceTest {

    @Mock
    UserBalanceRepository repository;

    @InjectMocks
    private UserBalanceService service;

    // --------------------------------
    // getBalanceOf
    // --------------------------------

    @Test
    void shouldReturnBalance_whenUserExists() {
        UserBalance userBalance = new UserBalance(10, 200);
        Mockito.when(repository.findById(userBalance.getUserId()))
                .thenReturn(Optional.of(userBalance));

        UserBalanceResponseDto response = this.service.getBalanceOf(10);
        Mockito.verify(repository).findById(userBalance.getUserId());
        assertEquals(response.getBalance(), userBalance.getBalance());
        assertEquals(response.getUserId(), userBalance.getUserId());
    }

    @Test
    void shouldThrow_whenUserBalanceNotFoundInGetBalance() {
        Integer userId = 99;
        Mockito.when(repository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserBalanceNotFoundException.class, () -> this.service.getBalanceOf(userId));
        Mockito.verify(repository).findById(userId);
    }

    // --------------------------------
    // deductPurchase
    // --------------------------------

    @Test
    void shouldThrow_whenUserHasNOTEnoughBalance() {
        UserBalance userBalance = new UserBalance(1, 50);
        Mockito.when(repository.findById(userBalance.getUserId()))
                .thenReturn(Optional.of(userBalance));

        assertThrows(InsufficientBalanceException.class,
                () -> service.deductPurchase(userBalance.getUserId(), 51));
        Mockito.verify(repository).findById(userBalance.getUserId());
    }

    @Test
    void shouldThrow_whenUserBalanceDoesNotExist() {
        Integer userId = 1;
        Mockito.when(repository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserBalanceNotFoundException.class,
                () -> service.deductPurchase(userId, 51));
        Mockito.verify(repository, times(1)).findById(userId);
    }

    @Test
    void shouldDeductBalance_whenSufficientFunds() {
        UserBalance ub = new UserBalance(1, 1000);
        Mockito.when(repository.findById(ub.getUserId())).thenReturn(Optional.of(ub));

        service.deductPurchase(ub.getUserId(), 500);
        assertEquals(500, ub.getBalance());
    }
    // --------------------------------
    // createUserBalance
    // --------------------------------

    @Test
    void shouldCreateUserBalanceSuccessfully() {
        var ub = new UserBalance(1, 1000);
        Mockito.when(repository.save(ub)).thenReturn(ub);

        UserBalanceResponseDto responseDto = this.service
                .createUserBalance(new UserBalanceRequestDto(
                        ub.getUserId(),
                        ub.getBalance()));
        Mockito.verify(repository).save(Mockito.any(UserBalance.class));

        assertEquals(ub.getBalance(), responseDto.getBalance());
        assertEquals(ub.getUserId(), responseDto.getUserId());
    }
}
