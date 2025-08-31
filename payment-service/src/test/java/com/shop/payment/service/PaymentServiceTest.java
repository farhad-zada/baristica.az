package com.shop.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.shop.common.event.OrderEvent;
import com.shop.common.event.PaymentEvent;
import com.shop.payment.exception.InsufficientBalanceException;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private UserBalanceService userBalanceService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PaymentService paymentService;

    OrderEvent orderEvent;

    @BeforeEach
    void setup() {
        orderEvent = new OrderEvent();
        orderEvent.setOrderId(1);
        orderEvent.setUserId(1);
        orderEvent.setCost(100);
    }

    @Test
    void shouldPublishPaymentEvent_whenUserHasSufficientFunds() {
        doNothing().when(userBalanceService).deductPurchase(orderEvent.getUserId(), orderEvent.getCost());
        paymentService.paymentProcessor(orderEvent);

        verify(userBalanceService, times(1)).deductPurchase(orderEvent.getUserId(), orderEvent.getCost());
        ArgumentCaptor<PaymentEvent> argumentCaptor = ArgumentCaptor.forClass(PaymentEvent.class);

        verify(userBalanceService).deductPurchase(orderEvent.getUserId(), orderEvent.getCost());
        verify(eventPublisher).publishEvent(argumentCaptor.capture());
        PaymentEvent publishedEvent = argumentCaptor.getValue();

        assertEquals(orderEvent.getOrderId(), publishedEvent.getOrderId());
    }

    @Test
    void shouldPublishPaymentEvent_evenWhenUserDoNoPossessSufficientFunds() {
        doThrow(new InsufficientBalanceException(orderEvent.getOrderId())).when(userBalanceService).deductPurchase(orderEvent.getUserId(), orderEvent.getCost());
        try {
            paymentService.paymentProcessor(orderEvent);
        } catch (InsufficientBalanceException e) {
            // e.printStackTrace();
        }

        verify(userBalanceService, times(1)).deductPurchase(orderEvent.getUserId(), orderEvent.getCost());
        ArgumentCaptor<PaymentEvent> argumentCaptor = ArgumentCaptor.forClass(PaymentEvent.class);

        verify(userBalanceService).deductPurchase(orderEvent.getUserId(), orderEvent.getCost());
        verify(eventPublisher).publishEvent(argumentCaptor.capture());
        PaymentEvent publishedEvent = argumentCaptor.getValue();

        assertEquals(orderEvent.getOrderId(), publishedEvent.getOrderId());
    }

}
