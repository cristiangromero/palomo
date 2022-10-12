package com.proyecto.palomo.controller;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;
import com.proyecto.palomo.mapper.UserMapper;
import com.proyecto.palomo.service.ICrudService;
import com.proyecto.palomo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends CrudController<UserRequest, UserResponse> {

    private final IUserService service;
    private final UserMapper userMapper;

    @PostMapping("/{id}/add/{contact}")
    public ResponseEntity<UserResponse> addContact(@PathVariable("id") long id, @PathVariable("contact") String usernameOrEmail) throws Exception {
        service.addContact(id, usernameOrEmail);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/remove/{contact}")
    public ResponseEntity<UserResponse> removeContact(@PathVariable("id") long id, @PathVariable("contact") String usernameOrEmail) throws Exception {
        service.removeContact(id, usernameOrEmail);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/contact")
    public ResponseEntity<List<UserResponse>> getAllContacts(@PathVariable("id") long id) {
        return ResponseEntity.ok(service.getAllContacts(id).stream().map(this.userMapper::toResponse).collect(Collectors.toList()));
    }

    @Override
    protected ICrudService<UserRequest, UserResponse> service() {
        return service;
    }
}
