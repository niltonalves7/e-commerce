package com.project.ecomerce.client.dto.request;

import jakarta.validation.constraints.*;

public record CreateClientRequestDTO(
        @NotBlank (message = "Name is required") String name,
        @Email @NotBlank (message = "Invalid Email")String email,
        @NotBlank (message = "Password is required")String password
) {}