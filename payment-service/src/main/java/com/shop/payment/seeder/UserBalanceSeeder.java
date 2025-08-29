package com.shop.payment.seeder;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.shop.payment.entity.UserBalance;
import com.shop.payment.repository.UserBalanceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(value = 1) 
public class UserBalanceSeeder implements CommandLineRunner {

    UserBalanceRepository repository;

    public UserBalanceSeeder(UserBalanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) {
            log.info("UserBalance has already been seeded so skipping...");
            return;
        }
        repository.saveAll(
                List.of(
                        new UserBalance(10, 1000),
                        new UserBalance(11, 2000),
                        new UserBalance(12, 5000),
                        new UserBalance(13, 10000),
                        new UserBalance(14, 15000),
                        new UserBalance(15, 500)));

        log.info("UserBalance seeded!");
    }

}
