package com.project.ecommerce.domain.user.dto.response;

import com.project.ecommerce.shared.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterUserResponseDTO(
        UUID id,
        String name,
        String email,
        Role role,
        LocalDateTime createdAt
) {}