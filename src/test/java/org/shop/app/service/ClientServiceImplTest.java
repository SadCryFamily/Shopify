package org.shop.app.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.ViewClientDto;
import org.shop.app.entity.Client;
import org.shop.app.mapper.ClientMapper;
import org.shop.app.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    @SpyBean
    private ClientMapper clientMapper;

    @Test()
    void createNewClient() {

        CreateClientDto mockExpectedCreateClientDto = CreateClientDto.builder()
                .clientName("Test").build();

        when(clientRepository.existsByClientNameAndIsDeletedIsFalse("Test")).thenReturn(false);

        CreateClientDto actualCreateClientDto = clientService.createNewClient(mockExpectedCreateClientDto);

        assertEquals(mockExpectedCreateClientDto.getClientName(), actualCreateClientDto.getClientName());
    }

    @Test
    void viewClientById() {

        ViewClientDto mockExpectedViewClientDto = ViewClientDto.builder()
                .clientName("Test")
                .build();

        Client mockClient = clientMapper.toClientEntity(mockExpectedViewClientDto);
        when(clientRepository.findClientByClientIdAndIsDeletedIsFalse(1L)).thenReturn(Optional.of(mockClient));

        ViewClientDto actualViewClientDto = clientService.viewClientById(1L);

        assertEquals(mockExpectedViewClientDto.getClientName(), actualViewClientDto.getClientName());
    }

    @Test
    void deleteClientById() {

        boolean expectedStatus = true;

        when(clientRepository.existsByClientIdAndIsDeletedIsFalse(1L)).thenReturn(true);

        CreateClientDto mockClientDto = CreateClientDto.builder()
                .clientName("Test").build();
        Client mockClient = clientMapper.toClientEntity(mockClientDto);

        when(clientRepository.findClientByClientIdAndIsDeletedIsFalse(1L)).thenReturn(Optional.of(mockClient));

        Boolean actualStatus = clientService.deleteClientById(1L);

        assertEquals(expectedStatus, actualStatus);
    }
}