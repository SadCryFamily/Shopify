package org.shop.app.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.shop.app.TestVariables;
import org.shop.app.annotation.MockClient;
import org.shop.app.dto.CreateBuyOrderDto;
import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.PayOrderDto;
import org.shop.app.entity.Client;
import org.shop.app.entity.Order;
import org.shop.app.mapper.ClientMapper;
import org.shop.app.mapper.OrderMapper;
import org.shop.app.repository.ClientRepository;
import org.shop.app.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class ClientServiceImplTest {

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private OrderRepository orderRepository;

    @SpyBean
    private OrderMapper orderMapper;

    @SpyBean
    private ClientMapper clientMapper;

    @Test
    @MockClient
    void placeAnOrder() {

        CreateBuyOrderDto mockCreateBuyOrderDto = CreateBuyOrderDto.builder()
                .orderName(TestVariables.BASIC_ORDER.getTestProperty())
                .build();

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(TestVariables.BASIC_CLIENT_NAME.getTestProperty())
                .clientPassword(TestVariables.BASIC_CLIENT_PS.getTestProperty())
                .build();

        when(clientRepository.existsByClientName(mockCreateClientDto.getClientName())).thenReturn(true);

        Order mockOrder = orderMapper.toCreateBuyOrderDto(mockCreateBuyOrderDto);
        Optional<Order> optionalMockOrder = Optional.of(mockOrder);

        when(orderRepository.findByOrderName(mockCreateBuyOrderDto.getOrderName()))
                .thenReturn(optionalMockOrder);

        Client mockClient = clientMapper.toClientEntity(mockCreateClientDto);

        when(clientRepository.findByClientName(mockClient.getClientName())).thenReturn(mockClient);

        String expectedResponse = String.format("Order [%s] placed successful", mockOrder.getOrderName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String actualResponse =
                clientService.placeAnOrder(authentication, mockCreateBuyOrderDto);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @MockClient
    void payAnOrder() {

        Authentication mockAuthentication = SecurityContextHolder.getContext().getAuthentication();

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(TestVariables.BASIC_CLIENT_NAME.getTestProperty())
                .clientPassword(TestVariables.BASIC_CLIENT_PS.getTestProperty())
                .build();

        Mockito.when(clientRepository.existsByClientName(mockCreateClientDto.getClientName()))
                .thenReturn(true);

        PayOrderDto mockPayOrderDto = PayOrderDto.builder()
                .orderName(TestVariables.BASIC_ORDER.getTestProperty())
                .build();

        Client mockClient = clientMapper.toClientEntity(mockCreateClientDto);

        Order mockOrder = orderMapper.toOrderEntity(mockPayOrderDto);
        mockOrder.setClient(mockClient);
        Optional<Order> optionalMockOrder = Optional.of(mockOrder);

        Mockito.when(orderRepository.findByOrderName(mockPayOrderDto.getOrderName())).thenReturn(optionalMockOrder);

        String expectedResponse = String.format("Order [%s] successfully payed", mockPayOrderDto.getOrderName());
        String actualResponse = clientService.payAnOrder(mockAuthentication,mockPayOrderDto);

        assertEquals(expectedResponse, actualResponse);

    }
}