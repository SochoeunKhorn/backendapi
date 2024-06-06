package com.sochoeun.controller.auth;

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
                    () -> new RuntimeException("Role:%s not found".formatted(role)));
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

        // response register -> success
        /*AuthResponse response = new AuthResponse();
        response.setFirstName(save.getFirstname());
        response.setLastName(save.getLastname());
        response.setEmail(save.getEmail());
        response.setProfile(save.getProfile());
        response.setRoles(save.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));

        return response;*/
        String token = jwtService.generateToken(save);
        return AuthResponse.builder()
                .firstName(save.getFirstname())
                .lastName(save.getLastname())
                .email(save.getEmail())
                .profile(save.getProfile())
                .roles(save.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .token(token)
                .build();
    }

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        // generate token
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var token = jwtService.generateToken(user);
       // log.info("user{}",user.getStatus());
        var response = userRepository.findUserByEmail(request.getEmail());

        /*authResponse.setFirstName(response.getFirstname());
        authResponse.setLastName(response.getLastname());
        authResponse.setEmail(response.getEmail());
        authResponse.setProfile(response.getProfile());
        authResponse.setRoles(response.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));
*/
        String getToken = token;
        return AuthResponse.builder()
                .firstName(response.getFirstname())
                .lastName(response.getLastname())
                .email(response.getEmail())
                .profile(response.getProfile())
                .roles(response.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .token(getToken)
                .build();
    }
}
