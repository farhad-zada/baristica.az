package com.shop.notification.exception;

import com.shop.common.event.OrderEvent;

public class UnexpectedOrderStatus extends RuntimeException {
    public UnexpectedOrderStatus (OrderEvent event) {
        super("Unexpected order status for {}".formatted(event));
    }

}
