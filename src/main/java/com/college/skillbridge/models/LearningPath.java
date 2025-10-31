package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class LearningPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany
    @JoinTable(
        name = "learning_path_batches",
        joinColumns = @JoinColumn(name = "path_id"),
        inverseJoinColumns = @JoinColumn(name = "batch_id")
    )
    private Set<Batch> requiredBatches = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "learning_path_skills",
        joinColumns = @JoinColumn(name = "path_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> targetSkills = new HashSet<>();

    @Column(nullable = false)
    private Integer estimatedDurationMonths;

    @Column(nullable = false)
    private String careerTrack; // e.g., "Full Stack Developer", "Data Scientist"

    @OneToMany(mappedBy = "learningPath", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PathMilestone> milestones = new HashSet<>();

    // Helper methods
    public void addBatch(Batch batch) {
        requiredBatches.add(batch);
    }

    public void removeBatch(Batch batch) {
        requiredBatches.remove(batch);
    }

    public void addSkill(Skill skill) {
        targetSkills.add(skill);
    }

    public void removeSkill(Skill skill) {
        targetSkills.remove(skill);
    }

    public void addMilestone(PathMilestone milestone) {
        milestones.add(milestone);
        milestone.setLearningPath(this);
    }

    public void removeMilestone(PathMilestone milestone) {
        milestones.remove(milestone);
        milestone.setLearningPath(null);
    }
}