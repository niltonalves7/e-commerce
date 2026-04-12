package com.project.ecomerce.order.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOrderRequestDTO(
        @NotNull UUID clientId
) {}
