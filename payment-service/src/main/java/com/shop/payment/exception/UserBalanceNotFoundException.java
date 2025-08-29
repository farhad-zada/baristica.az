package com.shop.payment.exception;

public class UserBalanceNotFoundException extends RuntimeException{
    public UserBalanceNotFoundException(Integer userId) {
        super("User balance not found: " + userId);
    }
}
