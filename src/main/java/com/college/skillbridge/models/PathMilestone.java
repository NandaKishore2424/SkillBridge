package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PathMilestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer sequenceOrder;

    @ManyToOne
    @JoinColumn(name = "learning_path_id", nullable = false)
    private LearningPath learningPath;

    @OneToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    private boolean isRequired = true;
}