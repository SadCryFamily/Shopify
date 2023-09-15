package org.shop.app.service;

import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.LoginClientDto;
import org.shop.app.entity.Client;
import org.shop.app.entity.ClientRoles;
import org.shop.app.entity.Role;
import org.shop.app.jwt.JwtUtils;
import org.shop.app.mapper.ClientMapper;
import org.shop.app.pojo.JwtLoginResponse;
import org.shop.app.repository.ClientRepository;
import org.shop.app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private ClientRepository clientRepository;

    private RoleRepository roleRepository;

    private ClientMapper clientMapper;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;

    @Autowired
    public AuthServiceImpl(ClientRepository clientRepository, RoleRepository roleRepository, ClientMapper clientMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.clientMapper = clientMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    @Transactional
    public String createClientAccount(CreateClientDto createClientDto) {

        Client client = clientMapper.toClientEntity(createClientDto);

        String encodedPassword = passwordEncoder.encode(createClientDto.getClientPassword());
        client.setClientPassword(encodedPassword);

        Role defaultRole = roleRepository.findRoleByRoleName(ClientRoles.ROLE_USER);
        client.setRoles(Set.of(defaultRole));

        clientRepository.save(client);

        return "Customer successfully created!";
    }

    @Override
    public JwtLoginResponse loginClientAccount(LoginClientDto loginClientDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginClientDto.getClientName(),
                        loginClientDto.getClientPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new JwtLoginResponse(jwt, userDetails.getUsername());
    }
}
