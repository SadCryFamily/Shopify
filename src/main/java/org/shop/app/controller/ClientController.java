package org.shop.app.controller;

import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.ViewClientDto;
import org.shop.app.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/client")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateClientDto createNewClient(@RequestBody CreateClientDto createClientDto) {
        return clientService.createNewClient(createClientDto);
    }

    @GetMapping("/client")
    public ViewClientDto viewClient(@RequestParam("id") Long clientId) {
        return clientService.viewClientById(clientId);
    }

    @DeleteMapping("/client")
    public boolean deleteClientById(@RequestParam("id") Long clientId) {
        return clientService.deleteClientById(clientId);
    }

}
