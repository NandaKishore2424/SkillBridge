package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "syllabus_topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyllabusTopic {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String technologies;
    
    @ManyToOne
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;
}