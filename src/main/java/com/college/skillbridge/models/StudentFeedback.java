package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentFeedback {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;
    
    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;
    
    @Column(columnDefinition = "TEXT")
    private String comment;
    
    // Rating on a scale of 1-5
    private Integer rating;
    
    private LocalDateTime timestamp;
}