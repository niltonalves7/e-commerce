package com.project.ecomerce.client.mapper;

import com.project.ecomerce.client.dto.response.RegisterClientResponseDTO;
import com.project.ecomerce.client.dto.request.RegisterClientRequestDTO;
import com.project.ecomerce.client.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(RegisterClientRequestDTO request);

    RegisterClientResponseDTO toRegisterResponse(Client client);
}