package com.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.shop.*"})
@EntityScan(basePackages = {"com.shop.*"})
public class PaymentServiceApp {
    public static void main(String[] args) {
        System.out.println("987654321");
        SpringApplication.run(PaymentServiceApp.class, args);
    }
}