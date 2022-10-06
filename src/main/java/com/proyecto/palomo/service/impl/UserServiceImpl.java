package com.proyecto.palomo.service.impl;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;
import com.proyecto.palomo.service.IUserService;

import java.util.Optional;

public class UserServiceImpl implements IUserService {
    @Override
    public UserResponse create(UserRequest request) {
        return null;
    }

    @Override
    public Optional<UserResponse> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserResponse> update(long id, UserRequest request) {
        return Optional.empty();
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
