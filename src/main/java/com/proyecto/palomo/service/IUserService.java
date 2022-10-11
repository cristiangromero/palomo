package com.proyecto.palomo.service;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;
import com.proyecto.palomo.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserResponse create(UserRequest request) throws Exception;
    Optional<UserResponse> get(long id);
    Optional<UserResponse> update(long id, UserRequest request);
    boolean delete(long id);
    void addContact(long id, String usernameOrEmail) throws Exception;
    void removeContact(long id, String usernameOrEmail) throws Exception;

    List<User> getAllContacts(long userId);
}
