package com.project.ecomerce.domain.product.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDTO(

        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String imageUrl,

        LocalDateTime createdAt
) {}
