package com.project.ecomerce.client.dto.request;

import jakarta.validation.constraints.*;

public record LoginClientRequestDTO(
    @Email @NotBlank (message = "Invalid Email")String email,
    @NotBlank (message = "Password is required")String password
) {}
