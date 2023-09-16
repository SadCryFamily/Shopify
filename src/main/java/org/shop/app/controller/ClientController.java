package org.shop.app.controller;

import org.shop.app.dto.CreateBuyOrderDto;
import org.shop.app.dto.PayOrderDto;
import org.shop.app.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/place-order")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('USER')")
    public String placeAnOrder(Principal principal, @RequestBody @Valid CreateBuyOrderDto createBuyOrderDto) {
        return clientService.placeAnOrder(principal, createBuyOrderDto);
    }

    @PostMapping("/pay-order")
    @PreAuthorize("hasRole('USER')")
    public String payAnOrder(Principal principal, @RequestBody @Valid PayOrderDto payOrderDto) {
        return clientService.payAnOrder(principal, payOrderDto);
    }

}
