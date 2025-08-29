package com.shop.payment;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PaymentServiceApp {
    public static void main(String[] args) {
        
        new SpringApplicationBuilder(PaymentServiceApp.class)
                .properties("spring.config.name=application")
                .run(args);
    }
}