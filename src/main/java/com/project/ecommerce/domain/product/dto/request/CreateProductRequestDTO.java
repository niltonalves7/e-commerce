package com.project.ecomerce.domain.product.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateProductRequestDTO(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Description is required")
        @Size(max = 500)
        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Stock is required")
        @PositiveOrZero(message = "Stock cannot be negative")
        Integer stock,

        String imageUrl
) {}
