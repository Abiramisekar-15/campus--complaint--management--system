package com.smart.campuscomplaint.controller;

import com.smart.campuscomplaint.dto.LoginRequest;
import com.smart.campuscomplaint.dto.LoginResponse;
import com.smart.campuscomplaint.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
