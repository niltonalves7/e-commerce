package com.project.ecomerce.order.dto.response;

import com.project.ecomerce.common.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        UUID clientId,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemResponseDTO> items,
        LocalDateTime createdAt
) {}
