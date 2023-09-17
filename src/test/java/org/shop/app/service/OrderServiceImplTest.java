package org.shop.app.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.shop.app.dto.CreateOrderDto;
import org.shop.app.dto.ViewOrderDto;
import org.shop.app.entity.Order;
import org.shop.app.mapper.OrderMapper;
import org.shop.app.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.shop.app.enums.TestVariables.BASIC_ORDER;
import static org.shop.app.enums.TestVariables.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class OrderServiceImplTest {

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @SpyBean
    private OrderMapper orderMapper;

    @Test
    void createAnOrder() {

        CreateOrderDto mockCreateOrderDto = CreateOrderDto.builder()
                .orderName(BASIC_ORDER.getTestProperty())
                .orderUnitQuantity(Integer.valueOf(BASIC_ORDER_UNIT.getTestProperty()))
                .orderUnitPrice(new BigDecimal(BASIC_ORDER_PRICE.getTestProperty()))
                .build();

        Mockito.when(orderRepository.existsByOrderName(mockCreateOrderDto.getOrderName()))
                .thenReturn(false);

        String expectedResponse = String.format("Order [%s] successfully created", mockCreateOrderDto.getOrderName());
        String actualResponse = orderService.createAnOrder(mockCreateOrderDto);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    void viewAllAvailableOrders() {

        CreateOrderDto mockCreateOrderDto = CreateOrderDto.builder()
                .orderName(BASIC_ORDER.getTestProperty())
                .orderUnitQuantity(Integer.valueOf(BASIC_ORDER_UNIT.getTestProperty()))
                .orderUnitPrice(new BigDecimal(BASIC_ORDER_PRICE.getTestProperty()))
                .build();

        Order mockOrder = orderMapper.toOrderEntity(mockCreateOrderDto);
        List<Order> listMockOrders = List.of(mockOrder);

        Mockito.when(orderRepository.findAllByIsDeletedIsFalse()).thenReturn(listMockOrders);

        List<ViewOrderDto> expectedViewOrderDtoList = listMockOrders.stream()
                .map(order -> orderMapper.toViewOrderDto(order))
                .collect(Collectors.toList());

        List<ViewOrderDto> actualViewOrderDtoList = orderService.viewAllAvailableOrders();

        assertEquals(expectedViewOrderDtoList.size(), actualViewOrderDtoList.size());

        assertEquals(
                expectedViewOrderDtoList.get(0).getOrderName(),
                actualViewOrderDtoList.get(0).getOrderName()
        );

        assertEquals(
                expectedViewOrderDtoList.get(0).getOrderUnitPrice(),
                actualViewOrderDtoList.get(0).getOrderUnitPrice()
        );

        assertEquals(
                expectedViewOrderDtoList.get(0).getOrderUnitQuantity(),
                actualViewOrderDtoList.get(0).getOrderUnitQuantity()
        );

    }
}