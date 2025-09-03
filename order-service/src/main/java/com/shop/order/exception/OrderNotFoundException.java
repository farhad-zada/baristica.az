package com.shop.order.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Integer id) {
        super("order not found: (id={})".formatted(id));
    }
}
