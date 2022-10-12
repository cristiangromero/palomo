package com.proyecto.palomo.service.impl;

import com.proyecto.palomo.model.Role;
import com.proyecto.palomo.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var user = repository.findByUserNameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario \"" + username + "\" no existe."));

        return new User(user.getUserName(), user.getPassword(), toAuthorities(user.getRoles()));
    }

    private Set<GrantedAuthority> toAuthorities(final Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }
}
