package com.shop.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.shop.dto.OrderRequestDto;
import com.shop.dto.OrderResponseDto;
import com.shop.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "status")
    @Mapping(ignore = true, target = "createdAt")
    Order toEntity(OrderRequestDto dto);

    OrderResponseDto toDto(Order order);

}
