package com.shop.entity.interfaces;

import java.time.LocalDateTime;

public interface Order {
    public Integer getId();
    public Integer getUserId();
    public Integer getCost();
    public LocalDateTime getCreatedAt();
}
