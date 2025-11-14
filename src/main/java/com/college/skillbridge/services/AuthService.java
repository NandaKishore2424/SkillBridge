package com.college.skillbridge.services;

import com.college.skillbridge.dtos.AdminRegistrationRequest;
import com.college.skillbridge.dtos.AuthRequest;
import com.college.skillbridge.dtos.AuthResponse;
import com.college.skillbridge.dtos.RegisterRequest;
import com.college.skillbridge.dtos.StudentRegistrationRequest;
import com.college.skillbridge.dtos.TrainerRegistrationRequest;

public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
    AuthResponse register(RegisterRequest request);
    AuthResponse registerAdmin(AdminRegistrationRequest request);
    AuthResponse registerStudent(StudentRegistrationRequest request);
    AuthResponse registerTrainer(TrainerRegistrationRequest request);
    AuthResponse getCurrentUserProfile(String email);
}