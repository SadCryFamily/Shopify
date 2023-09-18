package org.shop.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.shop.app.annotation.MockClient;
import org.shop.app.annotation.RealRandomClient;
import org.shop.app.dto.CreateOrderDto;
import org.shop.app.entity.ClientRoles;
import org.shop.app.enums.ExceptionMessage;
import org.shop.app.jwt.JwtUtils;
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

import static org.hamcrest.Matchers.is;
import static org.shop.app.enums.TestVariables.BASIC_ORDER_PRICE;
import static org.shop.app.enums.TestVariables.BASIC_ORDER_UNIT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    private String jwtToken;

    @BeforeEach
    public void provideJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        jwtToken = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    @DisplayName("createAnOrder_200OK")
    @RealRandomClient(role = ClientRoles.ROLE_MODERATOR)
    void createAnOrder() throws Exception {

        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .orderName("createAnOrder")
                .orderUnitQuantity(Integer.valueOf(BASIC_ORDER_UNIT.getTestProperty()))
                .orderUnitPrice(new BigDecimal(BASIC_ORDER_PRICE.getTestProperty()))
                .build();

        String expectedResponse = String.format("Order [%s] successfully created", createOrderDto.getOrderName());

        mockMvc.perform(MockMvcRequestBuilders.post("/add-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderDto))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));

    }

    @Test
    @DisplayName("viewAllAvailableOrders_200OK")
    @RealRandomClient
    void viewAllAvailableOrders() throws Exception {

        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .orderName("viewAllAvailableOrders1")
                .orderUnitQuantity(Integer.valueOf(BASIC_ORDER_UNIT.getTestProperty()))
                .orderUnitPrice(new BigDecimal(BASIC_ORDER_PRICE.getTestProperty()))
                .build();

        orderService.createAnOrder(createOrderDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                    .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("createAnOrder_asUser_400xx")
    @RealRandomClient
    public void createAnOrderAsUser() throws Exception {

        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .orderName("createAnOrderAsUser")
                .orderUnitQuantity(Integer.valueOf(BASIC_ORDER_UNIT.getTestProperty()))
                .orderUnitPrice(new BigDecimal(BASIC_ORDER_PRICE.getTestProperty()))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/add-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderDto))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error", Matchers.is("Access Denied")))
                .andExpect(
                        jsonPath("$.message",
                        Matchers.is(ExceptionMessage.ACCOUNT_ACCESS_DENIED.getExceptionMessage()))
                );

    }

    @Test
    @DisplayName("createAnExistedOrder_400x")
    @RealRandomClient(role = ClientRoles.ROLE_MODERATOR)
    public void createAnExistedOrder() throws Exception {

        CreateOrderDto createOrderDto = CreateOrderDto.builder()
                .orderName("createAnExistedOrder")
                .orderUnitQuantity(Integer.valueOf(BASIC_ORDER_UNIT.getTestProperty()))
                .orderUnitPrice(new BigDecimal(BASIC_ORDER_PRICE.getTestProperty()))
                .build();

        orderService.createAnOrder(createOrderDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/add-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderDto))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath(
                        "$.errors[0]",
                        Matchers.is(ExceptionMessage.ORDER_ALREADY_CREATED.getExceptionMessage()))
                );

    }
}