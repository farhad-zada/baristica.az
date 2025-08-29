package com.shop.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.payment.dto.UserBalanceResponseDto;
import com.shop.payment.service.UserBalanceService;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("balance")
public class UserBalanceController {
    UserBalanceService service;

    public UserBalanceController(UserBalanceService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserBalanceResponseDto> getBalanceOf(@PathVariable @Valid @NonNull @Min(0) Integer userId) {
        return ResponseEntity.ok().body(this.service.getBalanceOf(userId));
    }
}
