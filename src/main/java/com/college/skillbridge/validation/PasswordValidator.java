package com.college.skillbridge.validation;

import java.util.regex.Pattern;

public class PasswordValidator {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 30;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,30}$"
    );

    public static void validate(String password) {
        if (password == null || password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                "Password must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters long"
            );
        }
        
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(
                "Password must contain at least one digit, one lowercase letter, " +
                "one uppercase letter, one special character, and no whitespace"
            );
        }
    }
}