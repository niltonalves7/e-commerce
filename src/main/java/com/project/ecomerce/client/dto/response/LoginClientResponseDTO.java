package com.project.ecomerce.client.dto.response;

import com.project.ecomerce.common.enums.Roles;

public record LoginClientResponseDTO(

    String token,
    String tokenType,  // ex: "Bearer"
    String name,
    String email,
    Roles role
){}
