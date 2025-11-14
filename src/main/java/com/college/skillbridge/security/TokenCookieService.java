package com.college.skillbridge.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class TokenCookieService {

    private final String accessCookieName;
    private final String refreshCookieName;
    private final boolean secureCookies;
    private final String sameSiteMode;
    private final String cookieDomain;
    private final Duration accessTokenTtl;
    private final Duration refreshTokenTtl;

    public TokenCookieService(
            @Value("${app.security.jwt.access-cookie-name:SB_ACCESS}") String accessCookieName,
            @Value("${app.security.jwt.refresh-cookie-name:SB_REFRESH}") String refreshCookieName,
            @Value("${app.security.jwt.cookie-secure:true}") boolean secureCookies,
            @Value("${app.security.jwt.cookie-same-site:Strict}") String sameSiteMode,
            @Value("${app.security.jwt.cookie-domain:}") String cookieDomain,
            @Value("${jwt.expiration:86400000}") long accessTokenValidityMs,
            @Value("${jwt.refresh.expiration:604800000}") long refreshTokenValidityMs) {
        this.accessCookieName = accessCookieName;
        this.refreshCookieName = refreshCookieName;
        this.secureCookies = secureCookies;
        this.sameSiteMode = sameSiteMode;
        this.cookieDomain = cookieDomain;
        this.accessTokenTtl = Duration.ofMillis(accessTokenValidityMs);
        this.refreshTokenTtl = Duration.ofMillis(refreshTokenValidityMs);
    }

    public ResponseCookie createAccessTokenCookie(String token) {
        return buildCookie(accessCookieName, token, accessTokenTtl);
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return buildCookie(refreshCookieName, token, refreshTokenTtl);
    }

    public ResponseCookie clearAccessTokenCookie() {
        return buildCookie(accessCookieName, "", Duration.ZERO);
    }

    public ResponseCookie clearRefreshTokenCookie() {
        return buildCookie(refreshCookieName, "", Duration.ZERO);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return extractCookieValue(request, accessCookieName);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return extractCookieValue(request, refreshCookieName);
    }

    private Optional<String> extractCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .filter(value -> value != null && !value.isBlank())
                .findFirst();
    }

    private ResponseCookie buildCookie(String name, String value, Duration maxAge) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secureCookies)
                .sameSite(sameSiteMode)
                .path("/")
                .maxAge(maxAge);
        if (cookieDomain != null && !cookieDomain.isBlank()) {
            builder.domain(cookieDomain);
        }
        return builder.build();
    }
}

