package com.project.ecommerce.domain.cart.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CartResponseDTO(
        UUID id,
        List<CartItemResponseDTO> items,
        BigDecimal total,
        LocalDateTime updatedAt
) {}