package com.shop.entity;

import java.time.LocalDateTime;

import com.shop.entity.interfaces.OrderInterface;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order implements OrderInterface {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id;
        Integer userId;
        Integer productId;
        @Min(0)
        @Max(1000_000_000)
        Integer cost;
        @Enumerated(value = EnumType.ORDINAL) 
        OrderStatus status;
        LocalDateTime createdAt;
}
