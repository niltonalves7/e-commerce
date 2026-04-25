package com.project.ecommerce.domain.category.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponseDTO(
        UUID id,
        String name,
        String description,
        LocalDateTime createdAt
) {}