package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "syllabi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Syllabus {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @OneToOne(mappedBy = "syllabus")
    private Batch batch;
    
    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL)
    private List<SyllabusTopic> topics;
}