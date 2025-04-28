package com.college.skillbridge.models;

import com.college.skillbridge.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "student_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSkill {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillLevel level;
}