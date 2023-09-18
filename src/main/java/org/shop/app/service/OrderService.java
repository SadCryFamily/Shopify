package org.shop.app.service;

import org.shop.app.dto.CreateOrderDto;
import org.shop.app.dto.ViewOrderDto;

import java.util.List;

public interface OrderService {

    String createAnOrder(CreateOrderDto createOrderDto);

    List<ViewOrderDto> viewAllAvailableOrders();

}
