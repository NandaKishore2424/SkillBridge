package com.college.skillbridge.models;

import com.college.skillbridge.enums.ProficiencyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    private ProficiencyLevel level;
    
    // Other existing fields...
}