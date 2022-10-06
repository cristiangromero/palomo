package com.proyecto.palomo.service.impl;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;
import com.proyecto.palomo.mapper.UserMapper;
import com.proyecto.palomo.repository.IUserRepository;
import com.proyecto.palomo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    public UserResponse create(UserRequest request) throws Exception {
        if (!request.passwordMatches()) {
            throw new Exception("Las contraseñas no coinciden.");
        }

        final var entity = mapper.toEntity(request);
        entity.setPassword(encoder.encode(request.getPassword()));

        return mapper.toResponse(repository.save(entity));
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
        entity.setPassword(encoder.encode(request.getPassword()));

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

    @Override
    public void addContact(long id, String usernameOrEmail) throws Exception {
        final var contact = repository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail);

        if (contact.isEmpty()) {
            throw new Exception("El contacto a añadir, no existe.");
        }

        final var user = repository.findById(id);

        if (user.isEmpty()) {
            throw new Exception("El usuario no existe.");
        }

        user.get().addContact(contact.get());
    }

    @Override
    public void removeContact(long id, String usernameOrEmail) throws Exception {
        final var user = repository.findById(id);

        if (user.isEmpty()) {
            throw new Exception("El usuario no existe.");
        }

        final var contact = repository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail);

        if (contact.isEmpty()) {
            throw new Exception("El contacto a remover, no existe.");
        }

        user.get().removeContact(contact.get());
    }
}
