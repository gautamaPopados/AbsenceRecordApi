package com.gautama.abscencerecordhitsbackend.api.controller;

import com.gautama.abscencerecordhitsbackend.api.dto.LoginDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RegisterDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.TokenDTO;
import com.gautama.abscencerecordhitsbackend.core.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@RequestBody @Valid RegisterDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO request) {
        return ResponseEntity.ok(authService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok("User logged out successfully");
    }


}

