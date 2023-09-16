package org.shop.app.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;


}
