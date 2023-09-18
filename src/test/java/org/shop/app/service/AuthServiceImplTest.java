package org.shop.app.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.shop.app.enums.TestVariables;
import org.shop.app.annotation.MockClient;
import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.LoginClientDto;
import org.shop.app.entity.ClientRoles;
import org.shop.app.entity.Role;
import org.shop.app.jwt.JwtUtils;
import org.shop.app.mapper.ClientMapper;
import org.shop.app.pojo.JwtLoginResponse;
import org.shop.app.repository.ClientRepository;
import org.shop.app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class AuthServiceImplTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @SpyBean
    private ClientMapper clientMapper;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void createClientAccount() {

        CreateClientDto mockCreateClient = CreateClientDto.builder()
                .clientName(TestVariables.BASIC_CLIENT_NAME.getTestProperty())
                .clientPassword(TestVariables.BASIC_CLIENT_PS.getTestProperty())
                .build();

        Mockito.when(clientRepository.existsByClientName(mockCreateClient.getClientName()))
                .thenReturn(false);

        Role mockRole = Role.builder()
                .roleName(ClientRoles.ROLE_USER)
                .build();

        Mockito.when(roleRepository.findRoleByRoleName(ClientRoles.ROLE_USER)).thenReturn(mockRole);

        String expectedResponse = "Customer successfully created!";
        String actualResponse = authService.createClientAccount(mockCreateClient);

        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @MockClient
    void loginClientAccount() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();

        LoginClientDto mockLoginClientDto = LoginClientDto.builder()
                .clientName(userDetails.getUsername())
                .clientPassword(userDetails.getPassword())
                .build();

        Mockito.when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);

        Mockito.when(jwtUtils.generateJwtToken(any(Authentication.class)))
                .thenReturn("templateJwtToken");

        JwtLoginResponse expectedLoginResponse = JwtLoginResponse.builder()
                .jwtToken("templateJwtToken")
                .username(userDetails.getUsername())
                .build();

        JwtLoginResponse actualLoginResponse = authService.loginClientAccount(mockLoginClientDto);

        assertEquals(expectedLoginResponse.getJwtToken(), actualLoginResponse.getJwtToken());
        assertEquals(expectedLoginResponse.getUsername(), actualLoginResponse.getUsername());

    }
}