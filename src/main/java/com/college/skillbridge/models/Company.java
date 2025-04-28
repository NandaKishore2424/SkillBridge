package com.college.skillbridge.models;

import com.college.skillbridge.enums.HiringType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    private String domain;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "hiring_type")
    private HiringType hiringType;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<CompanyHiringProcess> hiringProcesses;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<BatchCompanyMapping> batchMappings;
}