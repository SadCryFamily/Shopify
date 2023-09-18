package org.shop.app.service;

import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.LoginClientDto;
import org.shop.app.pojo.JwtLoginResponse;

public interface AuthService {

    String createClientAccount(CreateClientDto createClientDto);

    JwtLoginResponse loginClientAccount(LoginClientDto loginClientDto);

}
