package com.project.ecommerce.infra.exception.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {}