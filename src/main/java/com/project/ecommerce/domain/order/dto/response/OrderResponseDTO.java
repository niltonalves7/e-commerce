package com.project.ecomerce.domain.order.dto.response;

import com.project.ecomerce.shared.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        UUID userId,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemResponseDTO> items,
        LocalDateTime createdAt
) {}
