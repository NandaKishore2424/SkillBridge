package com.college.skillbridge.dto;

import lombok.Data;

@Data
public class StudentRequestDto {
    private String name;
    private String email;
    private String password;
    private Integer year;
    private String department;
    private Float cgpa;
    private Integer problemSolvedCount;
    private String githubLink;
    private String portfolioLink;
    private String resumeLink;
}