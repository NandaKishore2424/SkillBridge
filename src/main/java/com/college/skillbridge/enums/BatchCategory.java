package com.college.skillbridge.enums;

public enum BatchCategory {
    TECHNICAL_TRAINING("Technical Training"),
    SOFT_SKILLS("Soft Skills"),
    DOMAIN_SPECIFIC("Domain Specific"),
    CERTIFICATION_PREP("Certification Preparation"),
    INDUSTRY_READINESS("Industry Readiness");

    private final String displayName;

    BatchCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}