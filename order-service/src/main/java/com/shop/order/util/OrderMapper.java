package com.shop.order.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.shop.common.event.OrderEvent;
import com.shop.order.dto.OrderRequestDto;
import com.shop.order.dto.OrderResponseDto;
import com.shop.order.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "status")
    @Mapping(ignore = true, target = "createdAt")
    Order toEntity(OrderRequestDto dto);

    OrderResponseDto toDto(Order order);

    @Mapping(source = "id", target = "orderId")
    @Mapping(ignore = true, target = "message")
    OrderEvent toEvent(Order order);

    default Long map(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli() : Instant.now().toEpochMilli();
    }
}
