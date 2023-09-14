package org.shop.app.service;

import lombok.extern.slf4j.Slf4j;
import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.ViewClientDto;
import org.shop.app.entity.Client;
import org.shop.app.enums.ExceptionMessage;
import org.shop.app.exception.ClientAlreadyCreatedException;
import org.shop.app.exception.ClientAlreadyDeletedException;
import org.shop.app.exception.ClientUnableToFindException;
import org.shop.app.mapper.ClientMapper;
import org.shop.app.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    private ClientMapper clientMapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    @Transactional
    public CreateClientDto createNewClient(CreateClientDto createClientDto) {

        Client mappedClientEntity = clientMapper.toClientEntity(createClientDto);

        if (clientRepository.existsByClientNameAndIsDeletedIsFalse(mappedClientEntity.getClientName())) {

            log.error("ERROR CREATE Client By Name: {} Reason: {}", mappedClientEntity.getClientName(), ClientAlreadyCreatedException.class);
            throw new ClientAlreadyCreatedException(ExceptionMessage.CLIENT_ALREADY_CREATED.getExceptionMessage());
        }

        clientRepository.save(mappedClientEntity);

        log.info("CREATE Client By ID: {}, NAME: {}",
                mappedClientEntity.getClientId(),mappedClientEntity.getClientName());
        return createClientDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ViewClientDto viewClientById(Long clientId) {
        Optional<Client> optionalClient = clientRepository.findClientByClientIdAndIsDeletedIsFalse(clientId);

        if (optionalClient.isEmpty()) {
            log.error("ERROR GET Client By ID: {} Reason: {}", clientId, ClientUnableToFindException.class);
            throw new ClientUnableToFindException(ExceptionMessage.CLIENT_NOT_FOUND.getExceptionMessage());
        }

        log.info("GET CLIENT By ID: {}", clientId);
        return clientMapper.toViewClientDto(optionalClient.get());
    }

    @Override
    @Transactional
    public boolean deleteClientById(Long clientId) {

        if (!clientRepository.existsByClientIdAndIsDeletedIsFalse(clientId)) {
            log.error("ERROR DELETE Client By ID: {} Reason: {}", clientId, ClientAlreadyDeletedException.class);
            throw new ClientAlreadyDeletedException(ExceptionMessage.CLIENT_ALREADY_DELETED.getExceptionMessage());
        }

        Client client = clientRepository.findClientByClientIdAndIsDeletedIsFalse(clientId).get();
        client.setDeleted(true);

        log.info("DELETE Client By ID: {}", clientId);
        return true;
    }
}
