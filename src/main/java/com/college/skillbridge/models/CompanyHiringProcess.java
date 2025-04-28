package com.college.skillbridge.models;

import com.college.skillbridge.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "company_hiring_process")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyHiringProcess {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
    @Column(name = "round_number")
    private Integer roundNumber;
    
    @Column(name = "round_name")
    private String roundName;
    
    @Column(name = "topics_focused", columnDefinition = "TEXT")
    private String topicsFocused;
    
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;
}