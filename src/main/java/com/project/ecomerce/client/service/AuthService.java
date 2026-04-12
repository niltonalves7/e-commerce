package com.project.ecomerce.client.service;

import com.project.ecomerce.client.dto.request.LoginClientRequestDTO;
import com.project.ecomerce.client.dto.response.LoginClientResponseDTO;
import com.project.ecomerce.client.entity.Client;
import com.project.ecomerce.client.repository.ClientRepository;
import com.project.ecomerce.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(ClientRepository clientRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginClientResponseDTO login(LoginClientRequestDTO request) {

        Client client = clientRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password."));

        if (!passwordEncoder.matches(request.password(), client.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password.");
        }

        String token = jwtService.generateToken(client);

        return new LoginClientResponseDTO(
                token,
                "Bearer",
                client.getName(),
                client.getEmail(),
                client.getRole()
        );
    }
}