package com.shop.common.event;


import java.io.Serializable;

import com.shop.common.entity.interfaces.OrderInterface;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent implements OrderInterface, Serializable {
    Integer orderId;
    Integer userId;
    Integer productId;
    Integer cost;
    OrderStatus status;
    Long createdAt; 
    String message;
}
