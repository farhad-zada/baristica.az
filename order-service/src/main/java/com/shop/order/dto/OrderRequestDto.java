package com.shop.order.dto;

import java.time.LocalDateTime;

import com.shop.common.event.OrderStatus;
import com.shop.common.entity.interfaces.OrderInterface;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto implements OrderInterface {
    @NotNull
    Integer userId;
    @NotNull
    Integer productId;
    @NotNull
    @Positive
    Integer cost;

    public Integer getId() {
        throw new UnsupportedOperationException();
    }

    public OrderStatus getStatus() {
        throw new UnsupportedOperationException();
    }

    public LocalDateTime getCreatedAt() {
        throw new UnsupportedOperationException();
    }
}
