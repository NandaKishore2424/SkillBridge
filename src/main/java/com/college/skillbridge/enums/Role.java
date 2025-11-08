package com.college.skillbridge.enums;

public enum Role {
    ADMIN,
    TRAINER,
    STUDENT;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}

