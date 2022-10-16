package com.proyecto.palomo.dto.user;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserRegister {
    private String username;
    private String email;
    private String password;
    private Set<String> roles = new HashSet<>();
}
