package org.shop.app.controller;

import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.LoginClientDto;
import org.shop.app.pojo.JwtLoginResponse;
import org.shop.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public String createClientAccount(@RequestBody @Valid CreateClientDto createClientDto) {
        return authService.createClientAccount(createClientDto);
    }

    @PostMapping("/signin")
    public JwtLoginResponse loginClientAccount(@RequestBody @Valid LoginClientDto loginClientDto) {
        return authService.loginClientAccount(loginClientDto);
    }

}
