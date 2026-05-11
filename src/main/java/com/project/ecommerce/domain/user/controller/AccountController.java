package com.project.ecommerce.domain.user.controller;

import com.project.ecommerce.domain.user.dto.request.UpdatePasswordRequestDTO;
import com.project.ecommerce.domain.user.dto.request.UpdateProfileRequestDTO;
import com.project.ecommerce.domain.user.dto.response.UserResponseDTO;
import com.project.ecommerce.domain.user.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getAccountProfile() {
        return ResponseEntity.ok(accountService.getAccountProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateAccountProfile(
            @RequestBody @Valid UpdateProfileRequestDTO request) {
        return ResponseEntity.ok(accountService.updateAccountProfile(request));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updateAccountPassword(
            @RequestBody @Valid UpdatePasswordRequestDTO request) {
        accountService.updateAccountPassword(request);
        return ResponseEntity.noContent().build();
    }
}