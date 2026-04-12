package com.project.ecomerce.client.service;

import com.project.ecomerce.client.dto.request.RegisterClientRequestDTO;
import com.project.ecomerce.client.dto.response.RegisterClientResponseDTO;
import java.util.List;
import java.util.UUID;

public interface ClientService {
    RegisterClientResponseDTO createClient(RegisterClientRequestDTO clientDto);
    RegisterClientResponseDTO getClientById(UUID id);
    List<RegisterClientResponseDTO> getAllClients();
    RegisterClientResponseDTO updateClient(UUID id, RegisterClientRequestDTO clientDto);
    void deleteClient(UUID id);
}