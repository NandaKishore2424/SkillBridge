package com.college.skillbridge.services.impl;

import com.college.skillbridge.dtos.AuthRequest;
import com.college.skillbridge.dtos.AuthResponse;
import com.college.skillbridge.dtos.RegisterRequest;
import com.college.skillbridge.models.User;
import com.college.skillbridge.repositories.UserRepository;
import com.college.skillbridge.security.JwtTokenProvider;
import com.college.skillbridge.services.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        // Validate input
        validateLoginRequest(request);
        
        // Authenticate
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Generate token
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        String token = jwtTokenProvider.createToken(
            user.getEmail(),
            Collections.singletonList(user.getRole().name())
        );

        return AuthResponse.builder()
            .token(token)
            .user(user)
            .build();
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validate input
        validateRegistrationRequest(request);
        
        // Check if user exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .role(request.getRole())
            .build();

        user = userRepository.save(user);

        // Generate token
        String token = jwtTokenProvider.createToken(
            user.getEmail(),
            Collections.singletonList(user.getRole().name())
        );

        return AuthResponse.builder()
            .token(token)
            .user(user)
            .build();
    }

    private void validateLoginRequest(AuthRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    private void validateRegistrationRequest(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (request.getRole() == null) {
            throw new IllegalArgumentException("Role is required");
        }
    }
}