package com.proyecto.palomo.controller;

import com.proyecto.palomo.dto.user.UserRequest;
import com.proyecto.palomo.dto.user.UserResponse;
import com.proyecto.palomo.service.ICrudService;
import com.proyecto.palomo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends CrudController<UserRequest, UserResponse> {

    private final IUserService service;

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

    @Override
    protected ICrudService<UserRequest, UserResponse> service() {
        return service;
    }
}
