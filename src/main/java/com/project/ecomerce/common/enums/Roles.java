package com.project.ecomerce.common.enums;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;

    Roles(String role){
        this.role = role;
    }
}