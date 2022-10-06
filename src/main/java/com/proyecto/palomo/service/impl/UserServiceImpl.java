package com.proyecto.palomo.service.impl;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;
import com.proyecto.palomo.mapper.UserMapper;
import com.proyecto.palomo.repository.IUserRepository;
import com.proyecto.palomo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserResponse create(UserRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public Optional<UserResponse> get(long id) {
        return repository.findById(id).map(mapper::toResponse);
    }

    @Override
    public Optional<UserResponse> update(long id, UserRequest request) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }

        final var entity = mapper.toEntity(request);
        entity.setUserId(id);

        return Optional.of(mapper.toResponse(repository.save(entity)));
    }

    @Override
    public boolean delete(long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
