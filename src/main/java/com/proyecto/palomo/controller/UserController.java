package com.proyecto.palomo.controller;

import com.proyecto.palomo.dto.user.*;
import com.proyecto.palomo.dto.userstatus.UserStatusResponse;
import com.proyecto.palomo.security.jwt.JwtUtils;
import com.proyecto.palomo.service.IUserService;
import com.proyecto.palomo.service.IUserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final IUserService service;
    private final IUserStatusService userStatusService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody final UserRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable("id") final long id) {
        return ResponseEntity.of(service.get(id));
    }
    @GetMapping("/my")
    public ResponseEntity<UserResponse> getMy(@RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.of(service.get(getIdByToken(token)));
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getByUsername(@PathVariable("username") String username) {
        return ResponseEntity.of(service.getByUsername(username));
    }


    @PutMapping()
    public ResponseEntity<UserResponse> update(@RequestHeader(name = "Authorization") String token, @RequestBody final UserUpdate userUpdate) {
        return ResponseEntity.of(service.update(getIdByToken(token), userUpdate));
    }

    @PutMapping("/password")
    public ResponseEntity<UserResponse> changePassword(@RequestHeader(name = "Authorization") String token, @RequestBody final UserUpdatePassword password) throws Exception {
        return ResponseEntity.ok(service.changePassword(getIdByToken(token), password));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> delete(@PathVariable("id") final long id) {
        return service.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/add/{contact}")
    public ResponseEntity<ContactResponse> addContact(@RequestHeader(name = "Authorization") String token, @PathVariable("contact") String usernameOrEmail) throws Exception {
        return ResponseEntity.ok(service.addContact(getIdByToken(token), usernameOrEmail));
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<UserResponse>> getContacts(@RequestHeader(name = "Authorization") String token) {
        long id = getIdByToken(token);
        return ResponseEntity.ok(service.getAllContacts(id));
    }

    private long getIdByToken(String token) {
        return Long.parseLong(jwtUtils.getUserIdFromJwtToken(token.substring(7)).trim());
    }

    @DeleteMapping("/contacts/remove/{contact}")
    public ResponseEntity<ContactResponse> removeContact(@RequestHeader(name = "Authorization") String token, @PathVariable("contact") String usernameOrEmail) throws Exception {
        return ResponseEntity.ok(service.removeContact(getIdByToken(token), usernameOrEmail));
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<UserStatusResponse>> getStatuses() {
        return ResponseEntity.ok(userStatusService.getAll());
    }

    @PutMapping("/status/{statusId}")
    public ResponseEntity<UserResponse> updateStatus(@RequestHeader(name = "Authorization") String token, @PathVariable("statusId") long statusId) throws Exception {
        return ResponseEntity.of(service.updateStatus(getIdByToken(token), statusId));
    }


}
