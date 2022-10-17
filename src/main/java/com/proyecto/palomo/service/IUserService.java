package com.proyecto.palomo.service;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserResponse create(final UserRequest request) throws Exception;

    Optional<UserResponse> get(final long id);

    Optional<UserResponse> update(final long id, final UserRequest request);

    boolean delete(final long id);

    Optional<UserResponse> getByUsername(String username);
    void addContact(long id, String usernameOrEmail) throws Exception;
    void removeContact(long id, String usernameOrEmail) throws Exception;

    List<UserResponse> getAllContacts(long userId);
}
