package org.shop.app.service;

import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.ViewClientDto;

public interface ClientService {

    CreateClientDto createNewClient(CreateClientDto createClientDto);

    ViewClientDto viewClientById(Long clientId);

    boolean deleteClientById(Long clientId);

}
