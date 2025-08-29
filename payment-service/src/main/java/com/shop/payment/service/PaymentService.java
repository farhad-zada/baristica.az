package com.shop.payment.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.shop.common.entity.PaymentStatus;
import com.shop.common.event.PaymentEvent;
import com.shop.payment.entity.Order;
import com.shop.payment.exception.InsufficientBalanceException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentService {
    private UserBalanceService userBalanceService;
    private ApplicationEventPublisher eventPublisher;

    public PaymentService(UserBalanceService userBalanceService, ApplicationEventPublisher eventPublisher) {
        this.userBalanceService = userBalanceService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void paymentProcessor(Order order) {
        PaymentEvent event = new PaymentEvent();
        event.setOrderId(order.getId());
        try {
            this.userBalanceService.deductPurchase(order.getUserId(), order.getCost());
            event.setStatus(PaymentStatus.SUCCESSFUL);
        } catch (InsufficientBalanceException ex) {
            event.setStatus(PaymentStatus.FAILED);
            event.setMessage("insufficient balance");
        }
        this.eventPublisher.publishEvent(event);
        log.info("Payment {} for orderId={} (userId={})  - event queued for publishing after commit",
                event.getStatus(), order.getId(), order.getUserId());
    }
}
