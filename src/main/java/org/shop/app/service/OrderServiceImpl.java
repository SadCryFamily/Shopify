package org.shop.app.service;

import lombok.RequiredArgsConstructor;
import org.shop.app.dto.CreateOrderDto;
import org.shop.app.dto.ViewOrderDto;
import org.shop.app.entity.Order;
import org.shop.app.mapper.OrderMapper;
import org.shop.app.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public String createAnOrder(CreateOrderDto createOrderDto) {
        Order order = orderMapper.toOrderEntity(createOrderDto);
        orderRepository.save(order);
        return "Order successfully created";
    }

    @Override
    @Transactional
    public List<ViewOrderDto> viewAllAvailableOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toViewOrderDto)
                .collect(Collectors.toList());
    }
}
