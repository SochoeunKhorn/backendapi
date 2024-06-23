package com.sochoeun.controller.auth;

import com.sochoeun.exception.ResourceNotFoundException;
import com.sochoeun.model.Role;
import com.sochoeun.model.User;
import com.sochoeun.repository.RoleRepository;
import com.sochoeun.repository.UserRepository;
import com.sochoeun.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request){

        // get roles from request
        List<String> strRole =request.getRoles();
        List<Role> roles = new ArrayList<>();
        for (String role:strRole){
            Role getRole = roleRepository.findByName(role).orElseThrow(
                    () -> new ResourceNotFoundException("Role with id: %s not found.".formatted(role)));
            roles.add(getRole);
        }
        // save user to db
        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .status(true)
                .build();
        User save = userRepository.save(user);

        String token = jwtService.generateToken(save);
        var refresh_token = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .id(save.getId())
                .firstName(save.getFirstname())
                .lastName(save.getLastname())
                .email(save.getEmail())
                .profile(save.getProfile())
                .roles(save.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .token(token)
                .refreshToken(refresh_token)
                .build();
    }

    public AuthResponse login(LoginRequest request){

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
            );
            // generate token
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            var token = jwtService.generateToken(user);
            var refresh_token = jwtService.generateRefreshToken(user);
            // log.info("user{}",user.getStatus());
            var response = userRepository.findUserByEmail(request.getEmail());

            return AuthResponse.builder()
                    .id(user.getId())
                    .firstName(response.getFirstname())
                    .lastName(response.getLastname())
                    .email(response.getEmail())
                    .profile(response.getProfile())
                    .roles(response.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .token(token)
                    .refreshToken(refresh_token)
                    .build();
    }
}
