package com.project.ecommerce.domain.auth.dto.response;

public record AuthResponseDTO(
        String token,
        String type,
        String name,
        String email,
        String role
) {
    public AuthResponseDTO(String token, String name, String email, String role) {
        this(token, "Bearer", name, email, role);
    }
}