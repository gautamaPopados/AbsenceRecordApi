package com.gautama.abscencerecordhitsbackend.core.service;

import com.gautama.abscencerecordhitsbackend.api.config.JwtUtil;
import com.gautama.abscencerecordhitsbackend.api.dto.RegisterRequest;
import com.gautama.abscencerecordhitsbackend.api.enums.Role;
import com.gautama.abscencerecordhitsbackend.core.model.RevokedToken;
import com.gautama.abscencerecordhitsbackend.core.model.User;
import com.gautama.abscencerecordhitsbackend.core.repository.RevokedTokenRepository;
import com.gautama.abscencerecordhitsbackend.core.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RevokedTokenRepository revokedTokenRepository;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("User already exists!");
        }

        Role role = Role.USER;

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);

        userRepository.save(user);
        return jwtUtil.generateToken(user);
    }

    public String login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return jwtUtil.generateToken(user);
    }


    public void logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }

        String token = authHeader.substring(7);
        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(token);
        revokedTokenRepository.save(revokedToken);
    }
}