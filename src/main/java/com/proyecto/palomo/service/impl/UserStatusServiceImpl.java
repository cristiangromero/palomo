package com.proyecto.palomo.service.impl;

import com.proyecto.palomo.dto.userstatus.UserStatusRequest;
import com.proyecto.palomo.dto.userstatus.UserStatusResponse;
import com.proyecto.palomo.mapper.UserStatusMapper;
import com.proyecto.palomo.model.UserStatus;
import com.proyecto.palomo.repository.IUserStatusRepository;
import com.proyecto.palomo.service.IUserStatausService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements IUserStatausService {
    private final IUserStatusRepository repository;
    private final UserStatusMapper mapper;

    @Override
    public UserStatusResponse create(UserStatusRequest userStatusRequest) {
        return mapper.toResponse(repository.save(mapper.toEntity(userStatusRequest)));
    }

    @Override
    public List<UserStatusResponse> getAll() {
        return mapper.toResponses(repository.findAll());
    }

    @Override
    public Optional<UserStatusResponse> get(Long id) {
        return repository.findById(id).map(mapper::toResponse);
    }

    @Override
    public Optional<UserStatusResponse> update(long id, UserStatusRequest userStatusRequest) {
        if (!repository.existsById(id)){
            return Optional.empty();
        }
        var entity = mapper.toEntity(userStatusRequest);
        entity.setUserStatusId(id);
        return Optional.of(mapper.toResponse(repository.save(entity)));
    }

    @Override
    public JpaRepository<UserStatus, Long> repository() {
        return repository;
    }
}
