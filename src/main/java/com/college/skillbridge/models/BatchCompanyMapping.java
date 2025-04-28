package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "batch_company_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchCompanyMapping {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;
    
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}