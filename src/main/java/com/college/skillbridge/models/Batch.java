package com.college.skillbridge.models;

import com.college.skillbridge.enums.BatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "batches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Batch {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "duration_weeks")
    private Integer durationWeeks;
    
    @Enumerated(EnumType.STRING)
    private BatchStatus status;
    
    @OneToOne
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;
    
    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<StudentBatchHistory> studentBatchHistories;
    
    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<BatchCompanyMapping> companyMappings;
    
    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<BatchTrainer> trainers;
}