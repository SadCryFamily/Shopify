package org.shop.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.app.dto.CreateOrderDto;
import org.shop.app.dto.ViewOrderDto;
import org.shop.app.entity.Order;
import org.shop.app.enums.ExceptionMessage;
import org.shop.app.exception.OrderAlreadyCreatedException;
import org.shop.app.mapper.OrderMapper;
import org.shop.app.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public String createAnOrder(CreateOrderDto createOrderDto) {

        if (orderRepository.existsByOrderName(createOrderDto.getOrderName())) {
            log.error("ERROR CREATE ORDER by NAME: {}. Reason: {}", createOrderDto.getOrderName(), OrderAlreadyCreatedException.class);
            throw new OrderAlreadyCreatedException(ExceptionMessage.ORDER_ALREADY_CREATED.getExceptionMessage());
        }

        Order order = orderMapper.toOrderEntity(createOrderDto);

        orderRepository.save(order);

        log.info("CREATE ORDER by NAME: {} with QUANTITY: {}", order.getOrderName(), order.getOrderUnitQuantity());
        return String.format("Order [%s] successfully created", order.getOrderName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewOrderDto> viewAllAvailableOrders() {

        log.info("RETRIEVE ALL AVAILABLE ORDERS");
        return orderRepository.findAllByIsDeletedIsFalse().stream()
                .map(orderMapper::toViewOrderDto)
                .collect(Collectors.toList());
    }
}
