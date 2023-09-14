package org.shop.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.shop.app.dto.CreateClientDto;
import org.shop.app.dto.ViewClientDto;
import org.shop.app.entity.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    Client toClientEntity(CreateClientDto createClientDto);

    ViewClientDto toViewClientDto(Client client);

}
