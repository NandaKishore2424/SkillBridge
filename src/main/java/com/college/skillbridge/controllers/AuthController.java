package com.college.skillbridge.controllers;

import com.college.skillbridge.dtos.AdminRegistrationRequest;
import com.college.skillbridge.dtos.AuthRequest;
import com.college.skillbridge.dtos.AuthResponse;
import com.college.skillbridge.dtos.RegisterRequest;
import com.college.skillbridge.dtos.StudentRegistrationRequest;
import com.college.skillbridge.dtos.TrainerRegistrationRequest;
import com.college.skillbridge.models.RefreshToken;
import com.college.skillbridge.models.User;
import com.college.skillbridge.security.JwtTokenProvider;
import com.college.skillbridge.security.TokenCookieService;
import com.college.skillbridge.services.AuthService;
import com.college.skillbridge.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenCookieService tokenCookieService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(
            AuthService authService,
            TokenCookieService tokenCookieService,
            RefreshTokenService refreshTokenService,
            JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.tokenCookieService = tokenCookieService;
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return attachCookies(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return attachCookies(response);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody AdminRegistrationRequest request) {
        AuthResponse response = authService.registerAdmin(request);
        return attachCookies(response);
    }

    @PostMapping("/student/register")
    public ResponseEntity<AuthResponse> registerStudent(@RequestBody StudentRegistrationRequest request) {
        AuthResponse response = authService.registerStudent(request);
        return attachCookies(response);
    }

    @PostMapping("/trainer/register")
    public ResponseEntity<AuthResponse> registerTrainer(@RequestBody TrainerRegistrationRequest request) {
        AuthResponse response = authService.registerTrainer(request);
        return attachCookies(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        AuthResponse profile = authService.getCurrentUserProfile(authentication.getName());
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request) {
        String refreshTokenValue = tokenCookieService.extractRefreshToken(request)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token missing"));

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenValue)
            .map(refreshTokenService::verifyExpiration)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        User user = refreshToken.getUser();
        String newAccessToken = jwtTokenProvider.createToken(
            user.getEmail(),
            java.util.Collections.singletonList(user.getRole().getAuthority())
        );
        RefreshToken rotatedRefresh = refreshTokenService.createRefreshToken(user.getId());

        AuthResponse response = authService.getCurrentUserProfile(user.getEmail());
        response.setToken(newAccessToken);
        response.setRefreshToken(rotatedRefresh.getToken());

        return attachCookies(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        tokenCookieService.extractRefreshToken(request)
            .flatMap(refreshTokenService::findByToken)
            .ifPresent(token -> refreshTokenService.deleteByUserId(token.getUser().getId()));

        ResponseCookie clearAccess = tokenCookieService.clearAccessTokenCookie();
        ResponseCookie clearRefresh = tokenCookieService.clearRefreshTokenCookie();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, clearAccess.toString())
            .header(HttpHeaders.SET_COOKIE, clearRefresh.toString())
            .build();
    }

    private ResponseEntity<AuthResponse> attachCookies(AuthResponse response) {
        ResponseCookie accessCookie = tokenCookieService.createAccessTokenCookie(response.getToken());
        ResponseCookie refreshCookie = tokenCookieService.createRefreshTokenCookie(response.getRefreshToken());
        response.clearTokens();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .body(response);
    }
}