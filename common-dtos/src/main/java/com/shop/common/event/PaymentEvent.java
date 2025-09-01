package com.shop.common.event;

import java.io.Serializable;

import com.shop.common.entity.interfaces.PaymentInterface;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent implements PaymentInterface, Serializable {
    Integer orderId;
    PaymentStatus status;
    String message;
}
