package com.college.skillbridge.services.impl;

import com.college.skillbridge.dtos.AdminRegistrationRequest;
import com.college.skillbridge.dtos.AuthRequest;
import com.college.skillbridge.dtos.AuthResponse;
import com.college.skillbridge.dtos.RegisterRequest;
import com.college.skillbridge.dtos.StudentRegistrationRequest;
import com.college.skillbridge.dtos.TrainerRegistrationRequest;
import com.college.skillbridge.enums.Role;
import com.college.skillbridge.models.Admin;
import com.college.skillbridge.models.College;
import com.college.skillbridge.models.Student;
import com.college.skillbridge.models.Trainer;
import com.college.skillbridge.models.User;
import com.college.skillbridge.repositories.AdminRepository;
import com.college.skillbridge.repositories.CollegeRepository;
import com.college.skillbridge.repositories.StudentRepository;
import com.college.skillbridge.repositories.TrainerRepository;
import com.college.skillbridge.repositories.UserRepository;
import com.college.skillbridge.security.JwtTokenProvider;
import com.college.skillbridge.services.AuthService;
import com.college.skillbridge.services.RefreshTokenService;
import com.college.skillbridge.models.RefreshToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final CollegeRepository collegeRepository;
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final TrainerRepository trainerRepository;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenService refreshTokenService,
            CollegeRepository collegeRepository,
            AdminRepository adminRepository,
            StudentRepository studentRepository,
            TrainerRepository trainerRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.collegeRepository = collegeRepository;
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        // Validate input
        validateLoginRequest(request);
        
        // Authenticate
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Generate token
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        String token = jwtTokenProvider.createToken(
            user.getEmail(),
            Collections.singletonList(user.getRole().getAuthority())
        );
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return buildAuthResponse(user, token, refreshToken.getToken());
    }

    @Override
    @Transactional
    @SuppressWarnings({"DataFlowIssue", "null"})
    public AuthResponse register(RegisterRequest request) {
        // Validate input
        validateRegistrationRequest(request);
        
        // Check if user exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        Role role = parseRole(request.getRole());

        // Create new user
        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .role(role)
            .build();

        userRepository.save(user);

        User savedUser = userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new RuntimeException("User persistence failed"));

        // Generate token
        String token = jwtTokenProvider.createToken(
            savedUser.getEmail(),
            Collections.singletonList(savedUser.getRole().getAuthority())
        );
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser.getId());
        return buildAuthResponse(savedUser, token, refreshToken.getToken());
    }

    @Override
    @Transactional
    public AuthResponse registerAdmin(AdminRegistrationRequest request) {
        validateAdminRequest(request);

        if (userRepository.existsByEmail(request.getAdminEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        College college = resolveCollege(request);

        if (adminRepository.existsByCollege(college)) {
            throw new IllegalStateException("An admin already exists for this college");
        }

        User adminUser = User.builder()
            .name(request.getAdminName())
            .email(request.getAdminEmail().toLowerCase(Locale.ROOT))
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.ADMIN)
            .college(college)
            .build();
        userRepository.save(adminUser);

        Admin admin = new Admin();
        admin.setName(request.getAdminName());
        admin.setEmail(request.getAdminEmail().toLowerCase(Locale.ROOT));
        admin.setPassword(adminUser.getPassword());
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setRoleTitle(request.getRoleTitle());
        admin.setCollege(college);
        adminRepository.save(admin);

        String accessToken = jwtTokenProvider.createToken(
            adminUser.getEmail(),
            Collections.singletonList(Role.ADMIN.getAuthority()));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(adminUser.getId());
        return buildAuthResponse(adminUser, accessToken, refreshToken.getToken());
    }

    @Override
    @Transactional
    public AuthResponse registerStudent(StudentRegistrationRequest request) {
        validateStudentRequest(request);

        String normalizedEmail = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmail(normalizedEmail) || studentRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (studentRepository.existsByRegisterNumber(request.getRegisterNumber())) {
            throw new IllegalArgumentException("Register number already in use");
        }

        College college = loadCollegeById(request.getCollegeId());
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User studentUser = User.builder()
            .name(request.getName())
            .email(normalizedEmail)
            .password(encodedPassword)
            .role(Role.STUDENT)
            .college(college)
            .build();
        userRepository.save(studentUser);

        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(normalizedEmail);
        student.setPassword(encodedPassword);
        student.setYear(request.getYear());
        student.setDepartment(request.getDepartment());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setRegisterNumber(request.getRegisterNumber());
        student.setCollege(college);
        studentRepository.save(student);

        String token = jwtTokenProvider.createToken(
            studentUser.getEmail(),
            Collections.singletonList(Role.STUDENT.getAuthority()));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(studentUser.getId());
        return buildAuthResponse(studentUser, token, refreshToken.getToken());
    }

    @Override
    @Transactional
    public AuthResponse registerTrainer(TrainerRegistrationRequest request) {
        validateTrainerRequest(request);

        String normalizedEmail = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmail(normalizedEmail) || trainerRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (trainerRepository.existsByTeacherId(request.getTeacherId())) {
            throw new IllegalArgumentException("Teacher ID already in use");
        }

        College college = loadCollegeById(request.getCollegeId());
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User trainerUser = User.builder()
            .name(request.getName())
            .email(normalizedEmail)
            .password(encodedPassword)
            .role(Role.TRAINER)
            .college(college)
            .build();
        userRepository.save(trainerUser);

        Trainer trainer = new Trainer();
        trainer.setName(request.getName());
        trainer.setEmail(normalizedEmail);
        trainer.setPassword(encodedPassword);
        trainer.setDepartment(request.getDepartment());
        trainer.setPhoneNumber(request.getPhoneNumber());
        trainer.setTeacherId(request.getTeacherId());
        trainer.setSpecialization(request.getSpecialization());
        trainer.setBio(request.getBio());
        trainer.setCollege(college);
        trainerRepository.save(trainer);

        String token = jwtTokenProvider.createToken(
            trainerUser.getEmail(),
            Collections.singletonList(Role.TRAINER.getAuthority()));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(trainerUser.getId());
        return buildAuthResponse(trainerUser, token, refreshToken.getToken());
    }

    @Override
    public AuthResponse getCurrentUserProfile(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return buildProfileResponse(user);
    }

    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshTokenValue) {
        AuthResponse response = buildProfileResponse(user);
        response.setToken(accessToken);
        response.setRefreshToken(refreshTokenValue);
        return response;
    }

    private AuthResponse buildProfileResponse(User user) {
        AuthResponse response = new AuthResponse();
        response.setId(String.valueOf(user.getId()));
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setRole(user.getRole().name());
        if (user.getCollege() != null) {
            response.setCollegeId(String.valueOf(user.getCollege().getId()));
        }
        return response;
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

    private Role parseRole(String rawRole) {
        if (rawRole == null || rawRole.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }
        try {
            return Role.valueOf(rawRole.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid role: " + rawRole);
        }
    }

    private void validateAdminRequest(AdminRegistrationRequest request) {
        if (isBlank(request.getAdminName())) {
            throw new IllegalArgumentException("Admin name is required");
        }
        if (isBlank(request.getAdminEmail())) {
            throw new IllegalArgumentException("Admin email is required");
        }
        if (isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }
        if (isBlank(request.getCollegeName())) {
            throw new IllegalArgumentException("College name is required");
        }
        if (isBlank(request.getCollegeDomain())) {
            throw new IllegalArgumentException("College domain is required");
        }
    }

    private void validateStudentRequest(StudentRegistrationRequest request) {
        if (isBlank(request.getName())) {
            throw new IllegalArgumentException("Student name is required");
        }
        if (isBlank(request.getEmail())) {
            throw new IllegalArgumentException("Student email is required");
        }
        if (isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }
        if (isBlank(request.getRegisterNumber())) {
            throw new IllegalArgumentException("Register number is required");
        }
        if (isBlank(request.getDepartment())) {
            throw new IllegalArgumentException("Department is required");
        }
        if (isBlank(request.getCollegeId())) {
            throw new IllegalArgumentException("College is required");
        }
    }

    private void validateTrainerRequest(TrainerRegistrationRequest request) {
        if (isBlank(request.getName())) {
            throw new IllegalArgumentException("Trainer name is required");
        }
        if (isBlank(request.getEmail())) {
            throw new IllegalArgumentException("Trainer email is required");
        }
        if (isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }
        if (isBlank(request.getTeacherId())) {
            throw new IllegalArgumentException("Teacher ID is required");
        }
        if (isBlank(request.getDepartment())) {
            throw new IllegalArgumentException("Department is required");
        }
        if (isBlank(request.getCollegeId())) {
            throw new IllegalArgumentException("College is required");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private College resolveCollege(AdminRegistrationRequest request) {
        return collegeRepository.findByDomain(request.getCollegeDomain().toLowerCase(Locale.ROOT))
            .orElseGet(() -> collegeRepository.save(College.builder()
                .name(request.getCollegeName())
                .domain(request.getCollegeDomain().toLowerCase(Locale.ROOT))
                .websiteUrl(request.getCollegeWebsite())
                .contactEmail(request.getCollegeContactEmail())
                .contactPhone(request.getCollegeContactPhone())
                .address(request.getCollegeAddress())
                .build()));
    }

    private College loadCollegeById(String collegeId) {
        try {
            UUID id = UUID.fromString(collegeId);
            return collegeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("College not found"));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid college identifier");
        }
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }
}