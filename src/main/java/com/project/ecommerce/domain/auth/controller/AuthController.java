package com.project.ecommerce.domain.auth.controller;

import com.project.ecommerce.domain.auth.dto.request.LoginRequestDTO;
import com.project.ecommerce.domain.auth.dto.response.AuthResponseDTO;
import com.project.ecommerce.domain.auth.service.AuthService;
import com.project.ecommerce.domain.user.dto.request.CreateUserRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid CreateUserRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}