package org.shop.app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.app.annotation.MockClient;
import org.shop.app.dto.CreateBuyOrderDto;
import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.PayOrderDto;
import org.shop.app.entity.Client;
import org.shop.app.entity.Order;
import org.shop.app.exception.ClientUnableToFindException;
import org.shop.app.exception.NotMyOrderToPayException;
import org.shop.app.exception.OrderClientAlreadyPresentException;
import org.shop.app.exception.OrderNullException;
import org.shop.app.mapper.ClientMapper;
import org.shop.app.mapper.OrderMapper;
import org.shop.app.repository.ClientRepository;
import org.shop.app.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.shop.app.enums.TestVariables.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ClientMapper clientMapper;

    @Test
    @MockClient
    void placeAnOrder() {

        CreateBuyOrderDto mockCreateBuyOrderDto = CreateBuyOrderDto.builder()
                .orderName(BASIC_ORDER.getTestProperty())
                .build();

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(NORMAL_CLIENT_NAME.getTestProperty())
                .clientPassword(NORMAL_CLIENT_PS.getTestProperty())
                .build();

        when(clientRepository.existsByClientName(mockCreateClientDto.getClientName())).thenReturn(true);

        Order mockOrder = orderMapper.toOrderEntity(mockCreateBuyOrderDto);
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
                .clientName(NORMAL_CLIENT_NAME.getTestProperty())
                .clientPassword(NORMAL_CLIENT_PS.getTestProperty())
                .build();

        when(clientRepository.existsByClientName(mockCreateClientDto.getClientName()))
                .thenReturn(true);

        PayOrderDto mockPayOrderDto = PayOrderDto.builder()
                .orderName(BASIC_ORDER.getTestProperty())
                .build();

        Client mockClient = clientMapper.toClientEntity(mockCreateClientDto);

        Order mockOrder = orderMapper.toOrderEntity(mockPayOrderDto);
        mockOrder.setClient(mockClient);
        Optional<Order> optionalMockOrder = Optional.of(mockOrder);

        when(orderRepository.findByOrderName(mockPayOrderDto.getOrderName())).thenReturn(optionalMockOrder);

        String expectedResponse = String.format("Order [%s] successfully payed", mockPayOrderDto.getOrderName());
        String actualResponse = clientService.payAnOrder(mockAuthentication, mockPayOrderDto);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @MockClient
    public void placeAnOrderByEmptyClient() {

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(NORMAL_CLIENT_NAME.getTestProperty())
                .clientPassword(NORMAL_CLIENT_PS.getTestProperty())
                .build();

        when(clientRepository.existsByClientName(mockCreateClientDto.getClientName()))
                .thenReturn(false);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CreateBuyOrderDto mockCreateBuyOrderDto = CreateBuyOrderDto.builder()
                .orderName(BASIC_ORDER.getTestProperty())
                .build();

        assertThrows(ClientUnableToFindException.class,
                () -> clientService.placeAnOrder(authentication, mockCreateBuyOrderDto));

    }

    @Test
    @MockClient
    public void placeAnEmptyOrder() {

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(NORMAL_CLIENT_NAME.getTestProperty())
                .clientPassword(NORMAL_CLIENT_PS.getTestProperty())
                .build();

        when(clientRepository.existsByClientName(mockCreateClientDto.getClientName())).thenReturn(true);

        CreateBuyOrderDto mockCreateBuyOrderDto = CreateBuyOrderDto.builder()
                .orderName(BASIC_ORDER.getTestProperty())
                .build();

        when(orderRepository.findByOrderName(mockCreateBuyOrderDto.getOrderName()))
                .thenReturn(Optional.empty());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThrows(OrderNullException.class,
                () -> clientService.placeAnOrder(authentication, mockCreateBuyOrderDto));

    }

    @Test
    @MockClient
    public void placeAnExistedOrder() {

        CreateBuyOrderDto mockCreateBuyOrderDto = CreateBuyOrderDto.builder()
                .orderName(BASIC_ORDER.getTestProperty())
                .build();

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(NORMAL_CLIENT_NAME.getTestProperty())
                .clientPassword(NORMAL_CLIENT_PS.getTestProperty())
                .build();

        when(clientRepository.existsByClientName(mockCreateClientDto.getClientName())).thenReturn(true);

        Client mockClient = clientMapper.toClientEntity(mockCreateClientDto);

        Order mockOrder = orderMapper.toOrderEntity(mockCreateBuyOrderDto);
        mockOrder.setClient(mockClient);

        Optional<Order> optionalMockOrder = Optional.of(mockOrder);

        when(orderRepository.findByOrderName(mockCreateBuyOrderDto.getOrderName()))
                .thenReturn(optionalMockOrder);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThrows(OrderClientAlreadyPresentException.class,
                () -> clientService.placeAnOrder(authentication, mockCreateBuyOrderDto));

    }

    @Test
    @MockClient
    public void payAnOrderOfEmptyClient() {

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(NORMAL_CLIENT_NAME.getTestProperty())
                .clientPassword(NORMAL_CLIENT_PS.getTestProperty())
                .build();

        when(clientRepository.existsByClientName(mockCreateClientDto.getClientName()))
                .thenReturn(false);

        PayOrderDto mockPayOrderDto = PayOrderDto.builder()
                .orderName(BASIC_ORDER.getTestProperty())
                .build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThrows(
                ClientUnableToFindException.class,
                () -> clientService.payAnOrder(authentication, mockPayOrderDto)
        );

    }

    @Test
    @MockClient
    public void payAnEmptyOrder() {

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(NORMAL_CLIENT_NAME.getTestProperty())
                .clientPassword(NORMAL_CLIENT_PS.getTestProperty())
                .build();

        when(clientRepository.existsByClientName(mockCreateClientDto.getClientName()))
                .thenReturn(true);

        PayOrderDto mockPayOrderDto = PayOrderDto.builder()
                .orderName(BASIC_ORDER.getTestProperty())
                .build();

        Order order = orderMapper.toOrderEntity(mockPayOrderDto);

        when(orderRepository.findByOrderName(order.getOrderName())).thenReturn(Optional.empty());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThrows(
                OrderNullException.class,
                () -> clientService.payAnOrder(authentication, mockPayOrderDto)
        );

    }

    @Test
    @MockClient(clientName = "foreignClient")
    public void payAnNotMyOrder() {

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(NORMAL_CLIENT_NAME.getTestProperty())
                .clientPassword(NORMAL_CLIENT_PS.getTestProperty())
                .build();

        when(clientRepository.existsByClientName(anyString())).thenReturn(true);

        PayOrderDto mockPayOrderDto = PayOrderDto.builder()
                .orderName(BASIC_ORDER.getTestProperty())
                .build();

        Client mockClient = clientMapper.toClientEntity(mockCreateClientDto);

        Order mockOrder = orderMapper.toOrderEntity(mockPayOrderDto);
        mockOrder.setClient(mockClient);
        Optional<Order> optionalMockOrder = Optional.of(mockOrder);

        when(orderRepository.findByOrderName(mockPayOrderDto.getOrderName()))
                .thenReturn(optionalMockOrder);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThrows(
                NotMyOrderToPayException.class,
                () -> clientService.payAnOrder(authentication, mockPayOrderDto)
        );

    }
}