package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class AssessmentQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(columnDefinition = "TEXT")
    private String correctAnswer;

    private String type; // MCQ, CODING, DESCRIPTIVE

    private Integer points;

    @Column(columnDefinition = "TEXT")
    private String options; // JSON array for MCQ options

    @ManyToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(columnDefinition = "TEXT")
    private String rubric; // Marking criteria for descriptive/coding questions
}