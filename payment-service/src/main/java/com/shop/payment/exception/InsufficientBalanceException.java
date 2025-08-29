package com.shop.payment.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(Integer userId) {
        super("insufficient balance");
    }

}
