package com.proyecto.palomo.service;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;
import com.proyecto.palomo.dto.user.UserUpdate;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserResponse create(final UserRequest request) throws Exception;

    Optional<UserResponse> get(final long id);

    Optional<UserResponse> update(final long id, final UserUpdate userUpdate);

    UserResponse changePassword(final long id, final String password) throws Exception;

    boolean delete(final long id);

    Optional<UserResponse> getByUsername(String username);
    void addContact(long id, String usernameOrEmail) throws Exception;
    void removeContact(long id, String usernameOrEmail) throws Exception;

    List<UserResponse> getAllContacts(long userId);
    Optional<UserResponse> updateStatus(long userId, long statusId) throws Exception;
}
