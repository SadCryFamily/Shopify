package org.shop.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.shop.app.dto.CreateBuyOrderDto;
import org.shop.app.dto.CreateOrderDto;
import org.shop.app.dto.PayOrderDto;
import org.shop.app.dto.ViewOrderDto;
import org.shop.app.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toOrderEntity(CreateOrderDto createOrderDto);

    Order toOrderEntity(PayOrderDto payOrderDto);

    ViewOrderDto toViewOrderDto(Order order);

    Order toCreateBuyOrderDto(CreateBuyOrderDto createBuyOrderDto);

}
