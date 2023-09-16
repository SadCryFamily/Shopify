package org.shop.app.controller;

import lombok.RequiredArgsConstructor;
import org.shop.app.service.ClientService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/hi")
    @PreAuthorize("hasRole('MODERATOR')")
    public String viewClientAccount(Principal principal) {
        return "Hello, " + principal.getName();
    }

}
