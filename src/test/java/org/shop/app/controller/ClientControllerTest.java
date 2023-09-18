package org.shop.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.shop.app.annotation.RealRandomClient;
import org.shop.app.dto.CreateBuyOrderDto;
import org.shop.app.dto.CreateOrderDto;
import org.shop.app.dto.PayOrderDto;
import org.shop.app.enums.ExceptionMessage;
import org.shop.app.jwt.JwtUtils;
import org.shop.app.mapper.ClientMapper;
import org.shop.app.mapper.OrderMapper;
import org.shop.app.service.ClientService;
import org.shop.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.shop.app.enums.TestVariables.BASIC_ORDER_PRICE;
import static org.shop.app.enums.TestVariables.BASIC_ORDER_UNIT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ClientService clientService;

    private String jwtToken;

    @BeforeEach
    public void provideJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        jwtToken = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    @RealRandomClient
    @DisplayName("placeOrder_200OK")
    void placeAnOrder() throws Exception {

        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .orderName("placeAnOrder")
                .orderUnitQuantity(Integer.valueOf(BASIC_ORDER_UNIT.getTestProperty()))
                .orderUnitPrice(new BigDecimal(BASIC_ORDER_PRICE.getTestProperty()))
                .build();

        orderService.createAnOrder(createOrderDto);

        CreateBuyOrderDto createBuyOrderDto = CreateBuyOrderDto.builder().orderName("placeAnOrder").build();

        String expectedResponse = String.format("Order [%s] placed successful", createBuyOrderDto.getOrderName());

        mockMvc.perform(MockMvcRequestBuilders.post("/place-order")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createBuyOrderDto))
                    .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));

    }

    @Test
    @DisplayName("payAnOrder_200OK")
    @RealRandomClient
    void payAnOrder() throws Exception {

        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .orderName("payAnOrder")
                .orderUnitQuantity(Integer.valueOf(BASIC_ORDER_UNIT.getTestProperty()))
                .orderUnitPrice(new BigDecimal(BASIC_ORDER_PRICE.getTestProperty())).build();

        orderService.createAnOrder(createOrderDto);

        CreateBuyOrderDto createBuyOrderDto = CreateBuyOrderDto.builder()
                .orderName("payAnOrder").build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        clientService.placeAnOrder(authentication, createBuyOrderDto);

        PayOrderDto payOrderDto = PayOrderDto.builder().orderName("payAnOrder").build();

        String expectedResponse = String.format("Order [%s] successfully payed",payOrderDto.getOrderName());

        mockMvc.perform(MockMvcRequestBuilders.post("/pay-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payOrderDto))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));

    }

    @Test
    @DisplayName("placeAnEmptyOrder_400xx")
    @RealRandomClient
    public void placeAnEmptyOrder() throws Exception {

        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .orderName("placeAnEmptyOrder")
                .orderUnitQuantity(Integer.valueOf(BASIC_ORDER_UNIT.getTestProperty()))
                .orderUnitPrice(new BigDecimal(BASIC_ORDER_PRICE.getTestProperty()))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/place-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderDto))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        jsonPath("$.errors[0]",
                        Matchers.is(ExceptionMessage.ORDER_NULL.getExceptionMessage()))
                );

    }

    @Test
    @DisplayName("payAnEmptyOrder_400xx")
    @RealRandomClient
    public void payAnEmptyOrder() throws Exception {

        PayOrderDto payOrderDto = PayOrderDto.builder().orderName("payAnEmptyOrder").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/pay-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payOrderDto))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath(
                        "$.errors[0]",
                        Matchers.is(ExceptionMessage.ORDER_NULL.getExceptionMessage()))
                );

    }
}