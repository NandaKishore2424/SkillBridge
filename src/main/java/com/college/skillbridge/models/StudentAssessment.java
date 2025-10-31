package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class StudentAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    private LocalDateTime startTime;

    private LocalDateTime submitTime;

    private Double score;

    @Column(columnDefinition = "TEXT")
    private String answers; // JSON containing answers for each question

    private String status; // NOT_STARTED, IN_PROGRESS, SUBMITTED, EVALUATED

    @Column(columnDefinition = "TEXT")
    private String feedback;
}