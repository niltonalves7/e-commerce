package com.project.ecommerce.domain.user.dto.request;

import jakarta.validation.constraints.*;

public record LoginUserRequestDTO(
    @Email @NotBlank (message = "Invalid Email")String email,
    @NotBlank (message = "Password is required")String password
) {}
