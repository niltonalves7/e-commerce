package com.project.ecommerce.domain.product.dto.response;

import com.project.ecommerce.domain.category.dto.response.CategoryResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        CategoryResponseDTO category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}