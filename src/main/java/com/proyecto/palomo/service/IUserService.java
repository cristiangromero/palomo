package com.proyecto.palomo.service;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;

public interface IUserService extends ICrudService<UserRequest, UserResponse> {
    void addContact(long id, String usernameOrEmail) throws Exception;
    void removeContact(long id, String usernameOrEmail) throws Exception;
}
