package com.college.skillbridge.services;

import com.college.skillbridge.dtos.AuthRequest;
import com.college.skillbridge.dtos.AuthResponse;
import com.college.skillbridge.dtos.RegisterRequest;

public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
    AuthResponse register(RegisterRequest request);
}