package com.proyecto.palomo.dto.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String usernameOrEmail, password;
}
