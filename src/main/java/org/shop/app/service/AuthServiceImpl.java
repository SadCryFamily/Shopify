package org.shop.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.LoginClientDto;
import org.shop.app.entity.Client;
import org.shop.app.entity.ClientRoles;
import org.shop.app.entity.Role;
import org.shop.app.enums.ExceptionMessage;
import org.shop.app.exception.ClientAlreadyCreatedException;
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
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ClientRepository clientRepository;

    private final RoleRepository roleRepository;

    private final ClientMapper clientMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public String createClientAccount(CreateClientDto createClientDto) {

        String username = createClientDto.getClientName();

        if (clientRepository.existsByClientName(username)) {
            log.error("ERROR CREATE Client By USERNAME: {}", username);
            throw new ClientAlreadyCreatedException(ExceptionMessage.CLIENT_ALREADY_CREATED.getExceptionMessage());
        }

        Client client = clientMapper.toClientEntity(createClientDto);

        String encodedPassword = passwordEncoder.encode(createClientDto.getClientPassword());
        client.setClientPassword(encodedPassword);

        Role defaultRole = roleRepository.findRoleByRoleName(ClientRoles.ROLE_USER);
        client.setRoles(Set.of(defaultRole));

        clientRepository.save(client);

        log.info("CREATE Client By USERNAME: {}", client.getClientName());
        return "Customer successfully created!";
    }

    @Override
    @Transactional
    public JwtLoginResponse loginClientAccount(LoginClientDto loginClientDto) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginClientDto.getClientName(),
                        loginClientDto.getClientPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        log.info("LOGIN Client By USERNAME: {}", userDetails.getClientName());
        return new JwtLoginResponse(jwt, userDetails.getUsername());
    }
}
