package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String type; // PRE_ASSESSMENT, PROGRESS_CHECK, FINAL_EVALUATION

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer durationMinutes;

    private Double passingScore;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AssessmentQuestion> questions = new HashSet<>();

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
    private Set<StudentAssessment> studentAssessments = new HashSet<>();

    // Helper methods
    public void addQuestion(AssessmentQuestion question) {
        questions.add(question);
        question.setAssessment(this);
    }

    public void removeQuestion(AssessmentQuestion question) {
        questions.remove(question);
        question.setAssessment(null);
    }
}