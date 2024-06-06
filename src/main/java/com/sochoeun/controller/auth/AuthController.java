package com.sochoeun.controller.auth;

import com.sochoeun.model.BaseResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private BaseResponse baseResponse;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        AuthResponse registered = authService.register(request);
        return ResponseEntity.ok(registered);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        AuthResponse login = authService.login(request);
        baseResponse  = new BaseResponse();
        baseResponse.success(login);
        return ResponseEntity.ok(baseResponse);
    }
}
