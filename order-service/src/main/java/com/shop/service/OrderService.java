package com.shop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.dto.OrderRequestDto;
import com.shop.dto.OrderResponseDto;
import com.shop.entity.Order;
import com.shop.entity.OrderStatus;
import com.shop.exceptions.OrderProcessingException;
import com.shop.repository.OrderRepository;
import com.shop.util.OrderMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

    OrderRepository repository;
    OrderMapper orderMapper;
    OrderEventService eventService;

    public OrderService(OrderRepository repository, OrderMapper orderMapper, OrderEventService eventService) {
        this.repository = repository;
        this.orderMapper = orderMapper;
        this.eventService = eventService;
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
        try {
            Order entity = orderMapper.toEntity(dto);
            entity.setStatus(OrderStatus.PENDING);
            entity.setCreatedAt(LocalDateTime.now());
            Order savedOrder = this.repository.save(entity);
            eventService.publishOrder(savedOrder);
            return orderMapper.toDto(savedOrder);
        } catch (Exception ex) {
            throw new OrderProcessingException("Order could not be saved: " + ex.getMessage());
        }

    }
}
