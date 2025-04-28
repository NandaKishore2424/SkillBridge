package com.college.skillbridge.models;

import com.college.skillbridge.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "training_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingProgress {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;
    
    @ManyToOne
    @JoinColumn(name = "syllabus_topic_id", nullable = false)
    private SyllabusTopic topic;
    
    @Enumerated(EnumType.STRING)
    private ProgressStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String feedback;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Trainer updatedBy;
}