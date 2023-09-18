package org.shop.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.shop.app.enums.TestVariables;
import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.LoginClientDto;
import org.shop.app.enums.ExceptionMessage;
import org.shop.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("create_newClient_200OK")
    void createClientAccount() throws Exception {

        CreateClientDto createClientDto = CreateClientDto.builder()
                .clientName("createClientAccount")
                .clientPassword(TestVariables.NORMAL_CLIENT_PS.getTestProperty()).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createClientDto)))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    @DisplayName("login_newClient_200OK")
    void loginClientAccount() throws Exception {

        CreateClientDto createClientDto = CreateClientDto.builder()
                .clientName("loginClientAccount")
                .clientPassword(TestVariables.NORMAL_CLIENT_PS.getTestProperty()).build();

        authService.createClientAccount(createClientDto);

        LoginClientDto loginClientDto = LoginClientDto.builder()
                .clientName("loginClientAccount")
                .clientPassword(TestVariables.NORMAL_CLIENT_PS.getTestProperty()).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginClientDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("create_existedNewClient_400xx")
    public void createExistedClientAccount() throws Exception {

        CreateClientDto createClientDto = CreateClientDto.builder()
                .clientName("createExistedClientAccount")
                .clientPassword(TestVariables.NORMAL_CLIENT_PS.getTestProperty()).build();

        authService.createClientAccount(createClientDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createClientDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(
                        jsonPath("$.errors[0]",
                        Matchers.is(ExceptionMessage.CLIENT_ALREADY_CREATED.getExceptionMessage()))
                );
    }

    @Test
    @DisplayName("login_emptyClientAccount_400xx")
    public void loginEmptyClientAccount() throws Exception {

        LoginClientDto loginClientDto = LoginClientDto.builder()
                .clientName("loginEmptyClientAccount")
                .clientPassword(TestVariables.NORMAL_CLIENT_PS.getTestProperty()).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginClientDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error", Matchers.is("Unauthorized")));

    }
}