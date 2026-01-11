package com.sofka.challenge.client_person.application.mapper;

import com.sofka.challenge.client_person.application.dto.ClientDTO;
import com.sofka.challenge.client_person.domain.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = PersonMapper.class)
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    ClientEntity toEntity(ClientDTO clientDTO);

    @Mapping(target = "name", source = "person.name")
    @Mapping(target = "gender", source = "person.gender")
    @Mapping(target = "age", source = "person.age")
    @Mapping(target = "identification", source = "person.identification")
    @Mapping(target = "address", source = "person.address")
    @Mapping(target = "phone", source = "person.phone")
    @Mapping(target = "password", ignore = true)
    ClientDTO toDTO(ClientEntity client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    void updateClientFromDto(ClientDTO clientDTO, @MappingTarget ClientEntity client);
}
