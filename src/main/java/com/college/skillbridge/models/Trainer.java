package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "trainers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String password;
    
    private String specialization;
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<BatchTrainer> batches;
}