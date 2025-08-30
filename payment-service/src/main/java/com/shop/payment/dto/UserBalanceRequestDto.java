package com.shop.payment.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserBalanceRequestDto {
    @Id
    Integer userId;

    @Column(nullable = false)
    @Min(0)
    Integer balance;
}
