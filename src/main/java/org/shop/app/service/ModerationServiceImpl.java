package org.shop.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.app.dto.ProvideAuthorityDto;
import org.shop.app.dto.RemoveAuthorityDto;
import org.shop.app.dto.ViewClientModerationDto;
import org.shop.app.entity.Client;
import org.shop.app.entity.ClientRoles;
import org.shop.app.entity.Role;
import org.shop.app.enums.ExceptionMessage;
import org.shop.app.exception.AuthorityAlreadyExistsException;
import org.shop.app.exception.ClientUnableToFindException;
import org.shop.app.mapper.ClientMapper;
import org.shop.app.repository.ClientRepository;
import org.shop.app.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModerationServiceImpl implements ModerationService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public List<ViewClientModerationDto> retrieveAllAuthorities() {

        return clientRepository.findAll().stream()
                .map(clientMapper::toViewClientModerationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String provideAuthoritiesTo(ProvideAuthorityDto provideAuthorityDto) {

        String clientName = provideAuthorityDto.getClientName();

        if (!clientRepository.existsByClientName(clientName)) {
            log.error("ERROR GRANT ROLE: {} to Client By USERNAME: {}. Reason: {} ",
                    ClientRoles.ROLE_MODERATOR, clientName, ClientUnableToFindException.class);
            throw new ClientUnableToFindException(ExceptionMessage.CLIENT_NOT_FOUND.getExceptionMessage());
        }

        Client client = clientRepository.findByClientName(clientName);

        Role moderatorRole = roleRepository.findRoleByRoleName(ClientRoles.ROLE_MODERATOR);
        Set<Role> currentRoles = client.getRoles();

        if (currentRoles.contains(moderatorRole)) {
            log.error("ERROR GRANT ROLE: {} to Client By USERNAME: {}. Reason: {} ",
                    ClientRoles.ROLE_MODERATOR, clientName, AuthorityAlreadyExistsException.class);
            throw new AuthorityAlreadyExistsException(ExceptionMessage.AUTH_ALREADY_EXISTS.getExceptionMessage());
        }

        currentRoles.add(moderatorRole);

        log.info("GRANT NEW ROLE: {} to Client By USERNAME: {}", ClientRoles.ROLE_MODERATOR, clientName);
        return String.format("Client [%s] successfully granted with Role [%s]",
                clientName, ClientRoles.ROLE_MODERATOR.extractRoleProperty());
    }

    @Override
    @Transactional
    public String removeAuthoritiesOf(RemoveAuthorityDto removeAuthorityDto) {

        String clientName = removeAuthorityDto.getClientName();

        if (!clientRepository.existsByClientName(clientName)) {
            log.error("ERROR GRANT ROLE: {} to Client By USERNAME: {}. Reason: {} ",
                    ClientRoles.ROLE_MODERATOR, clientName, ClientUnableToFindException.class);
            throw new ClientUnableToFindException(ExceptionMessage.CLIENT_NOT_FOUND.getExceptionMessage());
        }

        Client client = clientRepository.findByClientName(clientName);

        Role defaultRole = roleRepository.findRoleByRoleName(ClientRoles.ROLE_USER);
        Set<Role> defaultRoleSet = new HashSet<>();
        defaultRoleSet.add(defaultRole);

        client.setRoles(defaultRoleSet);

        log.info("REMOVED ROLE: {} to Client By USERNAME: {}", ClientRoles.ROLE_MODERATOR, clientName);
        return String.format("Client [%s] successfully waste the Role [%s]",
                clientName, ClientRoles.ROLE_MODERATOR.extractRoleProperty());
    }
}
