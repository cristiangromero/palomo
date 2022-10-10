package com.proyecto.palomo.dto.user;

import lombok.Data;

@Data
public class UserRequest {
    private String name, username, email, password, rPassword, picture;

    public boolean passwordMatches() {
        return password.equals(rPassword);
    }
}
