package com.project.ecommerce.domain.order.dto.request;

import com.project.ecommerce.shared.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDTO(
        @NotNull(message = "Status is required")
        OrderStatus status
) {}