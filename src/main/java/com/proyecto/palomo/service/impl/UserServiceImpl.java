package com.proyecto.palomo.service.impl;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;
import com.proyecto.palomo.enums.UserStatusEnum;
import com.proyecto.palomo.mapper.UserMapper;
import com.proyecto.palomo.model.Role;
import com.proyecto.palomo.model.User;
import com.proyecto.palomo.model.UserStatus;
import com.proyecto.palomo.repository.IRoleRepository;
import com.proyecto.palomo.repository.IUserRepository;
import com.proyecto.palomo.repository.IUserStatusRepository;
import com.proyecto.palomo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository repository;
    private final IRoleRepository roleRepository;
    private final IUserStatusRepository userStatusRepository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public UserResponse create(UserRequest request) throws Exception {
        final var user = repository.findByUserNameOrEmail(request.getUsername(), request.getEmail());

        if (user.isPresent()) {
            throw new Exception("El usuario ya se encuentra registrado");
        }

        if (!request.passwordMatches()) {
            throw new Exception("Las contraseñas no coinciden.");
        }

        final var entity = mapper.toEntity(request);
        entity.setPassword(encoder.encode(request.getPassword()));

        final var status = userStatusRepository.findByName(UserStatusEnum.AVAILABLE.getName());
        entity.setUserStatus(status.orElseGet(() -> new UserStatus(UserStatusEnum.AVAILABLE)));

        final var role = roleRepository.findByName("USER");
        entity.addRole(role.orElseGet(() -> new Role("USER")));

        return mapper.toResponse(repository.save(entity));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> get(long id) {
        return repository.findById(id).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public Optional<UserResponse> update(long id, UserRequest request) {
        final var user = repository.findById(id);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        final var entity = mapper.toEntity(request);
        entity.setUserId(id);
        entity.setPassword(encoder.encode(request.getPassword()));
        entity.setUserStatus(user.get().getUserStatus());

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
    @Transactional(readOnly = true)
    public Optional<UserResponse> getByUsername(String username) {
        return repository.findByUserName(username).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public void addContact(long id, String usernameOrEmail) throws Exception {
        final var contact = repository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new Exception("El contacto a añadir, no existe."));

        final var user = repository.findById(id)
                .orElseThrow(() -> new Exception("El usuario no existe."));

        user.addContact(contact);

        repository.save(user);
    }

    @Override
    @Transactional
    public void removeContact(long id, String usernameOrEmail) throws Exception {
        final var user = repository.findById(id)
                .orElseThrow(() -> new Exception("El usuario no existe."));

        final var contact = repository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new Exception("El contacto a remover, no existe."));

        user.removeContact(contact);

        repository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllContacts(long userId) {
        return mapper.toResponses(repository.findById(userId).orElseThrow().getContacts());
    }

    @Override
    @Transactional
    public Optional<UserResponse> updateStatus(long userId, long statusId) throws Exception {
        final var user = repository.findById(userId)
                .orElseThrow(() -> new Exception("No se ha encontrado al usuario"));

        final var statusEnum = Arrays.stream(UserStatusEnum.values())
                .filter(status -> status.getId() == statusId)
                .findFirst()
                .orElseThrow(() -> new Exception("Estado desconocido"));

        final var status = userStatusRepository.findByName(statusEnum.getName())
                .orElseThrow(() -> new Exception("Estado desconocido"));

        user.setUserStatus(status);

        return Optional.of(mapper.toResponse(repository.save(user)));
    }

}
