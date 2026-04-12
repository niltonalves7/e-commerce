package com.project.ecomerce.client.service;

import com.project.ecomerce.client.dto.request.RegisterClientRequestDTO;
import com.project.ecomerce.client.dto.response.RegisterClientResponseDTO;
import com.project.ecomerce.client.entity.Client;
import com.project.ecomerce.client.mapper.ClientMapper;
import com.project.ecomerce.client.repository.ClientRepository;
import com.project.ecomerce.common.enums.Roles;
import com.project.ecomerce.exception.AlreadyExistsException;
import com.project.ecomerce.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper mapper;
    private final PasswordEncoder passwordEncoder;

    private static final Roles DEFAULT_ROLE = Roles.USER;

    public ClientServiceImpl(ClientRepository clientRepository,
                             ClientMapper mapper,
                             PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterClientResponseDTO createClient(RegisterClientRequestDTO request) {

        if (clientRepository.findByEmail(request.email()).isPresent()){
                    throw new AlreadyExistsException("Email already registered");
                }

        Client client = mapper.toEntity(request);

        client.setPassword(passwordEncoder.encode(request.password()));
        client.setRole(DEFAULT_ROLE);

        Client saved = clientRepository.save(client);

        return mapper.toRegisterResponse(saved);
    }

    @Override
    public List<RegisterClientResponseDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(mapper::toRegisterResponse)
                .toList();
    }

    @Override
    public RegisterClientResponseDTO getClientById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found"));

        return mapper.toRegisterResponse(client);
    }

    @Override
    public RegisterClientResponseDTO updateClient(UUID id, RegisterClientRequestDTO clientDto) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found"));

        clientRepository.findByEmail(clientDto.email())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new AlreadyExistsException("User already registered");
                });

        client.setName(clientDto.name());
        client.setEmail(clientDto.email());

        if (clientDto.password() != null && !clientDto.password().isBlank()) {
            client.setPassword(passwordEncoder.encode(clientDto.password()));
        }
        Client updated = clientRepository.save(client);

        return mapper.toRegisterResponse(updated);
    }

    @Override
    public void deleteClient(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found"));

        clientRepository.delete(client);
    }
}