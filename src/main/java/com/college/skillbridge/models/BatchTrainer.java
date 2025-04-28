package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "batch_trainers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchTrainer {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;
    
    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;
    
    @Column(name = "role_description")
    private String roleDescription;
}