package com.shop.payment.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.shop.common.event.OrderEvent;
import com.shop.common.event.PaymentEvent;
import com.shop.payment.exception.InsufficientBalanceException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    public void paymentProcessor(OrderEvent orderEvent) {
        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setOrderId(orderEvent.getOrderId());
        try {
            this.userBalanceService.deductPurchase(orderEvent.getUserId(), orderEvent.getCost());
            paymentEvent.setMessage("payment acquired");
        } catch (InsufficientBalanceException ex) {
            paymentEvent.setMessage("insufficient balance");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        this.eventPublisher.publishEvent(paymentEvent);
        log.info("Payment {} for orderId={} (userId={})  - event queued for publishing after commit",
                paymentEvent.getStatus(), orderEvent.getOrderId(), orderEvent.getUserId());
    }
}
