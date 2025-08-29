package com.shop.payment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    Integer id;

    @Column(name = "user_id", nullable = false, updatable = false)
    Integer userId;

    @Column(name = "amount", nullable = false, updatable = false)
    @Min(0)
    Integer amount;
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createAt;

    @Column(name = "updated_at", nullable = true)
    LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true)
    LocalDateTime deletedAt;

    @PrePersist
    public void onCreate() {
        setCreateAt(LocalDateTime.now());
    }

    @PreUpdate
    public void onUpdate() {
        setUpdatedAt(LocalDateTime.now());
    }
}
