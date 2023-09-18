package org.shop.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.shop.app.annotation.RealRandomClient;
import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.ProvideAuthorityDto;
import org.shop.app.dto.RemoveAuthorityDto;
import org.shop.app.entity.ClientRoles;
import org.shop.app.enums.ExceptionMessage;
import org.shop.app.jwt.JwtUtils;
import org.shop.app.service.AuthService;
import org.shop.app.service.ModerationService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.shop.app.enums.TestVariables.NORMAL_CLIENT_NAME;
import static org.shop.app.enums.TestVariables.NORMAL_CLIENT_PS;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
class ModerationControllerTest {

    @Autowired
    private ModerationService moderationService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    private String jwtToken;

    @BeforeEach
    public void provideJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        jwtToken = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    @DisplayName("retrieve_allClientAuthorities_200OK")
    @RealRandomClient(role = ClientRoles.ROLE_MODERATOR)
    void retrieveAllClientAuthorities() throws Exception {

        CreateClientDto createClientDto = CreateClientDto.builder()
                .clientName("retrieveAllClientAuthorities")
                .clientPassword(NORMAL_CLIENT_PS.getTestProperty()).build();

        authService.createClientAccount(createClientDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/moderation/clients")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("provide_AuthoritiesTo_200OK")
    @RealRandomClient(role = ClientRoles.ROLE_MODERATOR)
    void provideAuthoritiesTo() throws Exception {

        CreateClientDto createClientDto = CreateClientDto.builder()
                .clientName("provideAuthoritiesTo")
                .clientPassword(NORMAL_CLIENT_PS.getTestProperty()).build();

        authService.createClientAccount(createClientDto);

        ProvideAuthorityDto provideAuthorityDto = ProvideAuthorityDto.builder()
                .clientName("provideAuthoritiesTo").build();

        String expectedResponse = String.format("Client [%s] successfully granted with Role [%s]",
                provideAuthorityDto.getClientName(), ClientRoles.ROLE_MODERATOR.extractRoleProperty());

        mockMvc.perform(MockMvcRequestBuilders.post("/moderation/grant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(provideAuthorityDto))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(expectedResponse));

    }

    @Test
    @DisplayName("remove_authoritiesOf_200OK")
    @RealRandomClient(role = ClientRoles.ROLE_MODERATOR)
    void removeAuthoritiesOf() throws Exception {

        CreateClientDto createClientDto = CreateClientDto.builder()
                .clientName("removeAuthoritiesOf")
                .clientPassword(NORMAL_CLIENT_NAME.getTestProperty()).build();

        authService.createClientAccount(createClientDto);

        ProvideAuthorityDto provideAuthorityDto = ProvideAuthorityDto.builder()
                .clientName(createClientDto.getClientName()).build();

        moderationService.provideAuthoritiesTo(provideAuthorityDto);

        RemoveAuthorityDto removeAuthorityDto = RemoveAuthorityDto.builder()
                .clientName(provideAuthorityDto.getClientName()).build();

        String expectedResponse = String.format("Client [%s] successfully waste the Role [%s]",
                removeAuthorityDto.getClientName(), ClientRoles.ROLE_MODERATOR.extractRoleProperty());

        mockMvc.perform(MockMvcRequestBuilders.post("/moderation/descent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(removeAuthorityDto))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(expectedResponse));

    }

    @Test
    @DisplayName("provide_AuthoritiesToEmptyClient_400xx")
    @RealRandomClient(role = ClientRoles.ROLE_MODERATOR)
    public void provideAuthoritiesToEmptyClient() throws Exception {

        ProvideAuthorityDto provideAuthorityDto = ProvideAuthorityDto.builder()
                .clientName("provideAuthoritiesToEmptyClient").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/moderation/grant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(provideAuthorityDto))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath(
                        "$.errors[0]",
                        Matchers.is(ExceptionMessage.CLIENT_NOT_FOUND.getExceptionMessage()))
                );

    }

    @Test
    @DisplayName("provide_existedAuthoritiesTo_400xx")
    @RealRandomClient(role = ClientRoles.ROLE_MODERATOR)
    public void provideExistedAuthoritiesTo() throws Exception {

        CreateClientDto createClientDto = CreateClientDto.builder()
                .clientName("provideExistedAuthoritiesTo")
                .clientPassword(NORMAL_CLIENT_PS.getTestProperty()).build();

        authService.createClientAccount(createClientDto);

        ProvideAuthorityDto provideAuthorityDto = ProvideAuthorityDto.builder()
                .clientName(createClientDto.getClientName()).build();

        moderationService.provideAuthoritiesTo(provideAuthorityDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/moderation/grant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(provideAuthorityDto))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath(
                        "$.errors[0]",
                        Matchers.is(ExceptionMessage.AUTH_ALREADY_EXISTS.getExceptionMessage()))
                );
    }

    @Test
    @DisplayName("remove_AuthoritiesOfEmptyClient_400xx")
    @RealRandomClient(role = ClientRoles.ROLE_MODERATOR)
    public void removeAuthoritiesOfEmptyClient() throws Exception {

        RemoveAuthorityDto removeAuthorityDto = RemoveAuthorityDto.builder()
                .clientName("removeAuthoritiesOfEmptyClient").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/moderation/descent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(removeAuthorityDto))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath(
                        "$.errors[0]",
                        Matchers.is(ExceptionMessage.CLIENT_NOT_FOUND.getExceptionMessage()))
                );

    }
}