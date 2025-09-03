package com.shop.order.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.common.event.OrderEvent;
import com.shop.common.event.PaymentEvent;
import com.shop.common.event.PaymentStatus;
import com.shop.order.dto.OrderRequestDto;
import com.shop.order.dto.OrderResponseDto;
import com.shop.order.exception.OrderNotFoundException;
import com.shop.order.model.Order;
import com.shop.order.repository.OrderRepository;
import com.shop.order.util.OrderMapper;
import com.shop.common.event.OrderStatus;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {

    OrderRepository repository;
    OrderMapper orderMapper;
    ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository repository, OrderMapper orderMapper, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.orderMapper = orderMapper;
        this.eventPublisher = eventPublisher;
    }

    public List<OrderResponseDto> getOrders() {
        return repository
                .findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public OrderResponseDto getOrderById(Integer id) {
        Order order = this.repository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Order not found: " + id));
        return orderMapper.toDto(order);
    }

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto dto) {
        Order entity = orderMapper.toEntity(dto);
        entity.setStatus(OrderStatus.PENDING);
        entity.setCreatedAt(LocalDateTime.now());
        Order savedOrder = this.repository.save(entity);
        eventPublisher.publishEvent(savedOrder);
        return orderMapper.toDto(savedOrder);

    }

    @Transactional
    public void paymentProcessed(PaymentEvent event) {
        Order order = this.repository
                .findById(event.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(event.getOrderId()));
        if (PaymentStatus.SUCCESSFUL.equals(event.getStatus())) {
            order.setStatus(OrderStatus.COMPLETED);
        } else if (PaymentStatus.FAILED.equals(event.getStatus())) {
            order.setStatus(OrderStatus.CANCELLED);
        }
        OrderEvent orderEvent = new OrderEvent(
                event.getOrderId(),
                order.getUserId(),
                order.getProductId(),
                order.getCost(),
                order.getStatus(),
                Instant.now().toEpochMilli(),
                "");

        this.eventPublisher.publishEvent(orderEvent);
    }
}
