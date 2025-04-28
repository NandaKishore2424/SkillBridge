package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private Integer year;
    
    private String department;
    
    private Float cgpa;
    
    @Column(name = "problem_solved_count")
    private Integer problemSolvedCount;
    
    @Column(name = "github_link")
    private String githubLink;
    
    @Column(name = "portfolio_link")
    private String portfolioLink;
    
    @Column(name = "resume_link")
    private String resumeLink;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Project> projects;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentSkill> skills;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentBatchHistory> batchHistory;
}