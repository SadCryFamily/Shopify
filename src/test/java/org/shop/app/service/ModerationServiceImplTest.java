package org.shop.app.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.ProvideAuthorityDto;
import org.shop.app.dto.RemoveAuthorityDto;
import org.shop.app.dto.ViewClientModerationDto;
import org.shop.app.entity.Client;
import org.shop.app.entity.ClientRoles;
import org.shop.app.entity.Role;
import org.shop.app.exception.AuthorityAlreadyExistsException;
import org.shop.app.exception.ClientUnableToFindException;
import org.shop.app.mapper.ClientMapper;
import org.shop.app.repository.ClientRepository;
import org.shop.app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.shop.app.enums.TestVariables.BASIC_CLIENT_NAME;
import static org.shop.app.enums.TestVariables.BASIC_CLIENT_PS;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class ModerationServiceImplTest {

    @Autowired
    private ModerationService moderationService;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private RoleRepository roleRepository;

    @SpyBean
    private ClientMapper clientMapper;

    @Test
    void retrieveAllAuthorities() {

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(BASIC_CLIENT_NAME.getTestProperty())
                .clientPassword(BASIC_CLIENT_PS.getTestProperty())
                .build();

        Client mockClient = clientMapper.toClientEntity(mockCreateClientDto);
        Role mockRole = Role.builder().roleId(1L).roleName(ClientRoles.ROLE_USER).build();
        mockClient.setRoles(Set.of(mockRole));

        List<Client> mockClientList = List.of(mockClient);

        when(clientRepository.findAll()).thenReturn(mockClientList);

        List<ViewClientModerationDto> expectedViewClientList = mockClientList.stream()
                .map(client -> clientMapper.toViewClientModerationDto(client))
                .collect(Collectors.toList());

        List<ViewClientModerationDto> actualViewClientList = moderationService.retrieveAllAuthorities();

        Assert.assertEquals(expectedViewClientList.size(), actualViewClientList.size());
        Assert.assertEquals(
                expectedViewClientList.get(0).getClientName(),
                actualViewClientList.get(0).getClientName()
        );

        Assert.assertEquals(
                expectedViewClientList.get(0).getRoles(),
                actualViewClientList.get(0).getRoles()
        );
    }

    @Test
    void provideAuthoritiesTo() {

        ProvideAuthorityDto mockProvideAuthorityDto = ProvideAuthorityDto.builder()
                .clientName(BASIC_CLIENT_NAME.getTestProperty()).build();

        when(clientRepository.existsByClientName(mockProvideAuthorityDto.getClientName()))
                .thenReturn(true);

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(BASIC_CLIENT_NAME.getTestProperty())
                .clientPassword(BASIC_CLIENT_PS.getTestProperty()).build();

        Client mockClient = clientMapper.toClientEntity(mockCreateClientDto);

        Role mockRoleUser = Role.builder()
                .roleId(2L)
                .roleName(ClientRoles.ROLE_USER).build();

        Set<Role> mockClientRoles = new HashSet<>();
        mockClientRoles.add(mockRoleUser);

        mockClient.setRoles(mockClientRoles);

        when(clientRepository.findByClientName(mockProvideAuthorityDto.getClientName()))
                .thenReturn(mockClient);

        Role mockRoleModerator = Role.builder()
                .roleId(2L)
                .roleName(ClientRoles.ROLE_MODERATOR).build();

        when(roleRepository.findRoleByRoleName(ClientRoles.ROLE_MODERATOR)).thenReturn(mockRoleModerator);

        String expectedResponse = String.format("Client [%s] successfully granted with Role [%s]",
                mockProvideAuthorityDto.getClientName(),ClientRoles.ROLE_MODERATOR.extractRoleProperty());

        String actualResponse = moderationService.provideAuthoritiesTo(mockProvideAuthorityDto);

        Assert.assertEquals(expectedResponse, actualResponse);

    }

    @Test
    void removeAuthoritiesOf() {

        RemoveAuthorityDto mockRemoveAuthorityDto = RemoveAuthorityDto.builder()
                .clientName(BASIC_CLIENT_NAME.getTestProperty()).build();

        when(clientRepository.existsByClientName(mockRemoveAuthorityDto.getClientName()))
                .thenReturn(true);

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(BASIC_CLIENT_NAME.getTestProperty())
                .clientPassword(BASIC_CLIENT_PS.getTestProperty()).build();

        Client mockClient = clientMapper.toClientEntity(mockCreateClientDto);

        Role mockRoleUser = Role.builder()
                .roleId(1L)
                .roleName(ClientRoles.ROLE_USER).build();

        Role mockRoleModerator = Role.builder()
                .roleId(2L)
                .roleName(ClientRoles.ROLE_MODERATOR).build();

        Set<Role> mockClientRoles = new HashSet<>();
        mockClientRoles.add(mockRoleUser);
        mockClientRoles.add(mockRoleModerator);

        mockClient.setRoles(mockClientRoles);

        when(clientRepository.findByClientName(mockClient.getClientName()))
                .thenReturn(mockClient);

        when(roleRepository.findRoleByRoleName(ClientRoles.ROLE_MODERATOR))
                .thenReturn(mockRoleModerator);

        String expectedResponse = String.format("Client [%s] successfully waste the Role [%s]",
                mockClient.getClientName(), ClientRoles.ROLE_MODERATOR.extractRoleProperty());

        String actualResponse = moderationService.removeAuthoritiesOf(mockRemoveAuthorityDto);

        Assert.assertEquals(expectedResponse, actualResponse);

    }

    @Test
    public void retrieveAllEmptyAuthorities() {

        List<ViewClientModerationDto> expectedList = Collections.emptyList();
        List<ViewClientModerationDto> actualList = moderationService.retrieveAllAuthorities();

        Assert.assertEquals(expectedList.size(), actualList.size());

    }

    @Test
    public void provideAuthoritiesToEmptyClient() {

        ProvideAuthorityDto mockProvideAuthorityDto = ProvideAuthorityDto.builder()
                .clientName(BASIC_CLIENT_NAME.getTestProperty()).build();

        when(clientRepository.existsByClientName(mockProvideAuthorityDto.getClientName()))
                .thenReturn(false);

        Assert.assertThrows(
                ClientUnableToFindException.class,
                () -> moderationService.provideAuthoritiesTo(mockProvideAuthorityDto)
        );

    }

    @Test
    public void provideExistedAuthorities() {

        ProvideAuthorityDto mockProvideAuthorityDto = ProvideAuthorityDto.builder()
                .clientName(BASIC_CLIENT_NAME.getTestProperty()).build();

        when(clientRepository.existsByClientName(mockProvideAuthorityDto.getClientName()))
                .thenReturn(true);

        CreateClientDto mockCreateClientDto = CreateClientDto.builder()
                .clientName(BASIC_CLIENT_NAME.getTestProperty())
                .clientPassword(BASIC_CLIENT_PS.getTestProperty()).build();

        Client mockClient = clientMapper.toClientEntity(mockCreateClientDto);

        Role mockRoleUser = Role.builder()
                .roleId(1L)
                .roleName(ClientRoles.ROLE_USER).build();

        Role mockRoleModerator = Role.builder()
                .roleId(2L)
                .roleName(ClientRoles.ROLE_MODERATOR).build();

        when(roleRepository.findRoleByRoleName(ClientRoles.ROLE_MODERATOR)).thenReturn(mockRoleModerator);

        Set<Role> mockClientRoles = new HashSet<>();
        mockClientRoles.add(mockRoleUser);
        mockClientRoles.add(mockRoleModerator);

        mockClient.setRoles(mockClientRoles);

        when(clientRepository.findByClientName(mockProvideAuthorityDto.getClientName()))
                .thenReturn(mockClient);

        Assert.assertThrows(
                AuthorityAlreadyExistsException.class,
                () -> moderationService.provideAuthoritiesTo(mockProvideAuthorityDto)
        );

    }

    @Test
    public void removeAuthoritiesOfEmptyClient() {

        RemoveAuthorityDto mockRemoveAuthorityDto = RemoveAuthorityDto.builder()
                .clientName(BASIC_CLIENT_NAME.getTestProperty()).build();

        when(clientRepository.existsByClientName(mockRemoveAuthorityDto.getClientName()))
                .thenReturn(false);

        Assert.assertThrows(
                ClientUnableToFindException.class,
                () -> moderationService.removeAuthoritiesOf(mockRemoveAuthorityDto)
        );

    }
}