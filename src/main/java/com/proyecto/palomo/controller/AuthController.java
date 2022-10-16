package com.proyecto.palomo.controller;

import com.proyecto.palomo.dto.auth.AuthRequest;
import com.proyecto.palomo.dto.auth.JwtResponse;
import com.proyecto.palomo.dto.auth.NoticeResponse;
import com.proyecto.palomo.dto.user.UserRegister;
import com.proyecto.palomo.model.User;
import com.proyecto.palomo.repository.IUserRepository;
import com.proyecto.palomo.security.jwt.JwtUtils;
import com.proyecto.palomo.security.service.UserDetailsImpl;
import com.proyecto.palomo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

/*
    //  private final AuthenticationManager manager;

 //   @PostMapping("/login")
  //  public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        try {
         //   final var authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsernameOrEmail(), authRequest.getPassword()));
          //  SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok("Sesión iniciada correctamente.");
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
*/

    @Autowired
    IUserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsernameOrEmail(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUsername(),
                userDetails.getAuthorities()
        ));

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserRegister userRegister) {



        if (userRepository.existsByUserName(userRegister.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new NoticeResponse(new Date(), "Error: Username exists!"));
        }


        /* Create new user's account */
        User user = new User(userRegister.getUsername(),
                userRegister.getEmail(),
                encoder.encode(userRegister.getPassword()));

        userRepository.save(user);
        return ResponseEntity.ok(new NoticeResponse(new Date(),"User successfull!!"));
    }
}
