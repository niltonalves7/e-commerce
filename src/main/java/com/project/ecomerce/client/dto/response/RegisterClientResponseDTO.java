package com.project.ecomerce.client.dto.response;

import com.project.ecomerce.common.enums.Roles;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterClientResponseDTO(
        UUID id,
        String name,
        String email,
        Roles role,
        LocalDateTime createdAt
) {}