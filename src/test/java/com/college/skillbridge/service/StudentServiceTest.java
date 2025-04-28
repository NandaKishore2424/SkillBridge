package com.college.skillbridge.service;

import com.college.skillbridge.dto.BatchRecommendationDto;
import com.college.skillbridge.enums.BatchStatus;
import com.college.skillbridge.enums.ProficiencyLevel;
import com.college.skillbridge.models.*;
import com.college.skillbridge.repositories.*;
import com.college.skillbridge.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private BatchRepository batchRepository;
    
    @Mock
    private CompanyRepository companyRepository;
    
    @Mock
    private SyllabusRepository syllabusRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student testStudent;
    private List<Batch> availableBatches;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        // Setup student with skills
        studentId = UUID.randomUUID();
        testStudent = new Student();
        testStudent.setId(studentId);
        testStudent.setName("Test Student");
        testStudent.setEmail("test@example.com");
        
        Set<Skill> skills = new HashSet<>();
        skills.add(createSkill("Java", ProficiencyLevel.INTERMEDIATE));
        skills.add(createSkill("React", ProficiencyLevel.BEGINNER));
        skills.add(createSkill("SQL", ProficiencyLevel.ADVANCED));
        testStudent.setSkills(skills);
        
        // Create batches with different technologies
        availableBatches = new ArrayList<>();
        
        // Batch 1: Good match (Java + React)
        Batch javaBatch = createBatch("Java Fullstack", Arrays.asList("Java", "Spring", "Hibernate"));
        javaBatch.setCompanies(Collections.singletonList(createCompany("Tech Co", Arrays.asList("Java", "Backend"), true)));
        availableBatches.add(javaBatch);
        
        // Batch 2: Partial match (React only)
        Batch webBatch = createBatch("Web Development", Arrays.asList("JavaScript", "React", "Node.js"));
        webBatch.setCompanies(Collections.singletonList(createCompany("Web Co", Arrays.asList("Frontend", "JavaScript"), false)));
        availableBatches.add(webBatch);
        
        // Batch 3: No match
        Batch pythonBatch = createBatch("Python Data Science", Arrays.asList("Python", "Pandas", "NumPy"));
        pythonBatch.setCompanies(Collections.singletonList(createCompany("Data Co", Arrays.asList("Python", "Data Science"), true)));
        availableBatches.add(pythonBatch);
        
        // Mock repository responses
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
        when(batchRepository.findByStatusIn(any())).thenReturn(availableBatches);
    }
    
    private Skill createSkill(String name, ProficiencyLevel level) {
        Skill skill = new Skill();
        skill.setId(UUID.randomUUID());
        skill.setName(name);
        skill.setLevel(level);
        return skill;
    }
    
    private Batch createBatch(String name, List<String> technologies) {
        Batch batch = new Batch();
        batch.setId(UUID.randomUUID());
        batch.setName(name);
        batch.setStatus(BatchStatus.UPCOMING);
        
        Syllabus syllabus = new Syllabus();
        syllabus.setId(UUID.randomUUID());
        
        List<SyllabusTopic> topics = new ArrayList<>();
        for (String tech : technologies) {
            SyllabusTopic topic = new SyllabusTopic();
            topic.setId(UUID.randomUUID());
            topic.setName(tech);
            topic.setTechnologies(tech);
            topics.add(topic);
        }
        syllabus.setTopics(topics);
        batch.setSyllabus(syllabus);
        
        return batch;
    }
    
    private Company createCompany(String name, List<String> domains, boolean isHiring) {
        Company company = new Company();
        company.setId(UUID.randomUUID());
        company.setName(name);
        company.setDomains(domains);
        company.setCurrentlyHiring(isHiring);
        return company;
    }

    @Test
    void shouldRecommendBatchesBasedOnSkills() {
        // When
        List<BatchRecommendationDto> recommendations = studentService.getRecommendedBatches(studentId);
        
        // Then
        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
        
        // First recommendation should be Java Fullstack (best match)
        assertEquals("Java Fullstack", recommendations.get(0).getBatchName());
        
        // Java batch should have higher score than Web batch
        assertTrue(recommendations.get(0).getTotalScore() > recommendations.get(1).getTotalScore());
        
        // Python batch should not be in recommendations or have lowest score
        if (recommendations.size() > 2) {
            assertEquals("Python Data Science", recommendations.get(2).getBatchName());
            assertTrue(recommendations.get(2).getTotalScore() < recommendations.get(1).getTotalScore());
        }
    }
    
    @Test
    void shouldIncludeHiringCompaniesInRecommendationScore() {
        // When
        List<BatchRecommendationDto> recommendations = studentService.getRecommendedBatches(studentId);
        
        // Then
        // Verify the Java batch with hiring company has higher company score
        assertTrue(recommendations.get(0).getCompanyRelevanceScore() > 0);
        
        // The match reasons should mention companies hiring
        boolean mentionsHiring = recommendations.get(0).getMatchReasons().stream()
                .anyMatch(reason -> reason.contains("hiring"));
        
        assertTrue(mentionsHiring);
    }
    
    @Test
    void shouldReturnEmptyListWhenNoMatchesFound() {
        // Given a student with no matching skills
        Student noMatchStudent = new Student();
        noMatchStudent.setId(UUID.randomUUID());
        noMatchStudent.setSkills(Collections.singleton(createSkill("C++", ProficiencyLevel.BEGINNER)));
        
        when(studentRepository.findById(noMatchStudent.getId())).thenReturn(Optional.of(noMatchStudent));
        
        // When
        List<BatchRecommendationDto> recommendations = studentService.getRecommendedBatches(noMatchStudent.getId());
        
        // Then
        assertTrue(recommendations.isEmpty() || recommendations.stream().allMatch(r -> r.getTotalScore() == 0));
    }
}