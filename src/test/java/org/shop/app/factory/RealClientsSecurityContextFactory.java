package org.shop.app.factory;

import org.shop.app.annotation.RealRandomClient;
import org.shop.app.dto.CreateClientDto;
import org.shop.app.entity.Client;
import org.shop.app.entity.ClientRoles;
import org.shop.app.entity.Role;
import org.shop.app.mapper.ClientMapper;
import org.shop.app.repository.ClientRepository;
import org.shop.app.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RealClientsSecurityContextFactory implements WithSecurityContextFactory<RealRandomClient> {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SecurityContext createSecurityContext(RealRandomClient realRandomClient) {

        SecurityContext context = SecurityContextHolder.getContext();

        String generatedUsername = realRandomClient.clientName() + "_" + new Date().getTime();
        String encryptedPassword = passwordEncoder.encode(realRandomClient.clientPassword());

        CreateClientDto createClientDto = CreateClientDto.builder()
                .clientName(generatedUsername)
                .clientPassword(encryptedPassword).build();

        Client client = clientMapper.toClientEntity(createClientDto);

        Role defaultRole = Role.builder()
                .roleId(1)
                .roleName(ClientRoles.ROLE_USER).build();

        Set<Role> clientRoles = new HashSet<>();
        clientRoles.add(defaultRole);

        if (realRandomClient.role() != ClientRoles.ROLE_USER) {
            Role moderatorRole = Role.builder()
                    .roleId(2)
                    .roleName(ClientRoles.ROLE_USER).build();

            clientRoles.add(moderatorRole);
        }

        client.setRoles(clientRoles);

        clientRepository.save(client);

        Collection<? extends GrantedAuthority> authorities = clientRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toSet());

        UserDetailsImpl userDetails = new UserDetailsImpl(
                generatedUsername, encryptedPassword,
                Boolean.FALSE, authorities
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                encryptedPassword,
                authorities
        );

        context.setAuthentication(authentication);

        return context;

    }
}
