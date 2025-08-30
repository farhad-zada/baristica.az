package com.shop.order.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Integer id) {
        super("order not found: (id={})".formatted(id));
    }
}
