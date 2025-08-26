package com.shop.entity.interfaces;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.shop.entity.OrderStatus;

public interface OrderInterface extends Serializable {
    Integer getId();
    Integer getUserId();
    Integer getProductId();
    Integer getCost();
    OrderStatus getStatus();
    LocalDateTime getCreatedAt();
}
