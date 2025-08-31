package com.shop.payment.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.shop.common.event.OrderEvent;
import com.shop.common.event.PaymentEvent;

import org.springframework.transaction.annotation.Transactional;

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
        this.eventPublisher.publishEvent(paymentEvent);
        log.info("Payment {} for orderId={} (userId={})  - event queued for publishing after commit",
                paymentEvent.getStatus(), orderEvent.getOrderId(), orderEvent.getUserId());
        this.userBalanceService.deductPurchase(orderEvent.getUserId(), orderEvent.getCost());
    }
}
