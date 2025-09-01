package com.shop.payment.repository;


import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.shop.payment.entity.UserBalance;

@DataJpaTest
@ActiveProfiles("test")
public class UserBalanceRepositoryTest {

    @Autowired
    UserBalanceRepository repository;

    @Test
    @DisplayName("Should save & retrieve UserBalance entity")
    void shouldSaveAndFindByIdEntity() {
        UserBalance ub = new UserBalance(1, 200);
        UserBalance saved = this.repository.save(ub);
        assert ub.getUserId() == saved.getUserId();
        assert ub.getBalance() == saved.getBalance();
        Optional<UserBalance> found = repository.findById(ub.getUserId());
        assert found.isPresent();
    }

    @Test
    void shouldReturnEmptyForNonExistent() {
        Optional<UserBalance> optional = this.repository.findById(999);
        assert !optional.isPresent();
    }

}
