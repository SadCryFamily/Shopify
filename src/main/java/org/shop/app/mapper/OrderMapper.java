package org.shop.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.shop.app.dto.CreateOrderDto;
import org.shop.app.dto.ViewOrderDto;
import org.shop.app.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toOrderEntity(CreateOrderDto createOrderDto);

    ViewOrderDto toViewOrderDto(Order order);

}
