package com.project.ecommerce.domain.user.dto.request;

import com.project.ecommerce.shared.enums.Role;
import jakarta.validation.constraints.*;

public record RegisterUserRequestDTO(
        @NotBlank (message = "Name is required") String name,
        @Email @NotBlank (message = "Invalid Email")String email,
        @NotBlank (message = "Password is required")String password,
        Role role
) {}