package com.proyecto.palomo.service;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;
import com.proyecto.palomo.model.User;

import java.util.List;

public interface IUserService extends ICrudService<UserRequest, UserResponse> {
    void addContact(long id, String usernameOrEmail) throws Exception;
    void removeContact(long id, String usernameOrEmail) throws Exception;

    List<User> getAllContacts(long userId);
}
