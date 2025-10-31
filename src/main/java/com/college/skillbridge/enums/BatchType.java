package com.college.skillbridge.enums;

public enum BatchType {
    FULL_TIME("Full Time"),
    PART_TIME("Part Time"),
    WEEKEND("Weekend"),
    FAST_TRACK("Fast Track"),
    SPECIALIZED("Specialized");

    private final String displayName;

    BatchType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}