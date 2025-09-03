package com.shop.order.dto;

import java.time.LocalDateTime;

import com.shop.common.event.OrderStatus;
import com.shop.common.entity.interfaces.OrderInterface;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto implements OrderInterface {
    @NotNull
    private Integer id;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer productId;
    @NotNull
    private Integer cost;
    @NotNull
    protected OrderStatus status;
    @NotNull
    private LocalDateTime createdAt;
}   
