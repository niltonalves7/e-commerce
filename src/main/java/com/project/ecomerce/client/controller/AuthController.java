package com.project.ecomerce.client.controller;

import com.project.ecomerce.client.service.AuthService;
import com.project.ecomerce.client.dto.request.LoginClientRequestDTO;
import com.project.ecomerce.client.dto.response.LoginClientResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginClientResponseDTO> login(@RequestBody @Valid LoginClientRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}