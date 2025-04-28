package com.college.skillbridge.service.impl;

import com.college.skillbridge.dto.BatchRecommendationDto;
import com.college.skillbridge.dto.StudentRequestDto;
import com.college.skillbridge.enums.BatchStatus;
import com.college.skillbridge.enums.ProficiencyLevel;
import com.college.skillbridge.exception.ResourceNotFoundException;
import com.college.skillbridge.models.*;
import com.college.skillbridge.repositories.*;
import com.college.skillbridge.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final BatchRepository batchRepository;
    private final StudentBatchHistoryRepository studentBatchHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final SyllabusRepository syllabusRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    @Override
    public Student createStudent(StudentRequestDto studentDto) {
        Student student = new Student();
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setPassword(passwordEncoder.encode(studentDto.getPassword()));
        student.setYear(studentDto.getYear());
        student.setDepartment(studentDto.getDepartment());
        student.setCgpa(studentDto.getCgpa());
        student.setProblemSolvedCount(studentDto.getProblemSolvedCount());
        student.setGithubLink(studentDto.getGithubLink());
        student.setPortfolioLink(studentDto.getPortfolioLink());
        student.setResumeLink(studentDto.getResumeLink());
        
        return studentRepository.save(student);
    }

    @Override
    public List<Student> findStudentsBySkill(String skillName) {
        Skill skill = skillRepository.findByName(skillName)
                .orElseThrow(() -> new RuntimeException("Skill not found: " + skillName));
        
        List<StudentSkill> studentSkills = studentSkillRepository.findAll().stream()
                .filter(ss -> ss.getSkill().getId().equals(skill.getId()))
                .collect(Collectors.toList());
        
        return studentSkills.stream()
                .map(StudentSkill::getStudent)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Batch assignStudentToBestFitBatch(UUID studentId) {
        // Get the student
        Student student = getStudentById(studentId);
        
        // Get all batches
        List<Batch> allBatches = batchRepository.findAll();
        
        // Simple best-fit algorithm based on student skills/problem count
        Batch bestFitBatch = allBatches.stream()
                .max(Comparator.comparingInt(batch -> calculateBatchFitScore(batch, student)))
                .orElseThrow(() -> new RuntimeException("No suitable batch found"));
        
        // Create the student-batch relationship
        StudentBatchHistory batchHistory = new StudentBatchHistory();
        batchHistory.setStudent(student);
        batchHistory.setBatch(bestFitBatch);
        batchHistory.setStartDate(LocalDate.now());
        batchHistory.setStatus(BatchStatus.ACTIVE);
        
        studentBatchHistoryRepository.save(batchHistory);
        
        return bestFitBatch;
    }

    @Override
    public List<StudentBatchHistory> getStudentBatchHistory(UUID studentId) {
        Student student = getStudentById(studentId);
        return studentBatchHistoryRepository.findByStudent(student);
    }
    
    // Helper method to calculate fit score for batch assignment
    private int calculateBatchFitScore(Batch batch, Student student) {
        int score = 0;
        
        // Logic based on problem solved count
        if (student.getProblemSolvedCount() != null) {
            if (student.getProblemSolvedCount() > 200 && batch.getName().contains("DSA")) {
                score += 50;
            } else if (student.getProblemSolvedCount() > 100 && batch.getName().contains("Full Stack")) {
                score += 30;
            }
        }
        
        // Logic based on CGPA
        if (student.getCgpa() != null) {
            if (student.getCgpa() > 8.5) {
                score += 20;
            } else if (student.getCgpa() > 7.5) {
                score += 10;
            }
        }
        
        return score;
    }

    @Override
    public List<BatchRecommendationDto> getRecommendedBatches(UUID studentId) {
        // Get student with their skills
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        
        // Get all active/upcoming batches - Use ACTIVE and existing enum values
        List<Batch> availableBatches = batchRepository.findByStatusIn(
            Arrays.asList(BatchStatus.ACTIVE) // Use only existing enum values
        );
        
        // Calculate match score for each batch
        List<BatchRecommendationDto> recommendations = new ArrayList<>();
        
        for (Batch batch : availableBatches) {
            // Initialize scores
            int skillMatchScore = 0;
            int syllabusOverlapScore = 0;
            int companyRelevanceScore = 0;
            List<String> matchReasons = new ArrayList<>();
            
            // 1. Skill Match Scoring (weight: +2 per match)
            Set<String> batchTechnologies = getBatchTechnologies(batch);
            int matchedSkills = 0;
            
            // Fix StudentSkill issue - use the correct loop
            for (StudentSkill studentSkill : student.getSkills()) {
                Skill skill = studentSkill.getSkill();
                if (batchTechnologies.contains(skill.getName().toLowerCase())) {
                    matchedSkills++;
                    
                    // Add bonus for higher proficiency - assuming skill has level field
                    if (ProficiencyLevel.INTERMEDIATE.equals(skill.getLevel())) {
                        skillMatchScore += 1;
                    } else if (ProficiencyLevel.ADVANCED.equals(skill.getLevel())) {
                        skillMatchScore += 2;
                    }
                }
            }
            
            // Base skill match points: 2 points per skill match
            skillMatchScore += matchedSkills * 2;
            
            if (matchedSkills > 0) {
                matchReasons.add(matchedSkills + " of your skills match this batch's technologies");
            }
            
            // 2. Syllabus Overlap Scoring (weight: +3 per topic match)
            Syllabus syllabus = batch.getSyllabus();
            if (syllabus != null) {
                // Get student's previous batches to check what topics they've already covered
                Set<String> studentKnownTopics = getStudentKnownTopics(student);
                
                // Find new topics in this syllabus (learning opportunity)
                List<SyllabusTopic> newTopics = syllabus.getTopics().stream()
                    .filter(topic -> !studentKnownTopics.contains(topic.getName().toLowerCase()))
                    .collect(Collectors.toList());
                
                // Score based on new topics (learning opportunity)
                syllabusOverlapScore = newTopics.size() * 3;
                
                if (!newTopics.isEmpty()) {
                    matchReasons.add("You'll learn " + newTopics.size() + 
                                    " new topics including " + 
                                    newTopics.stream()
                                        .limit(2)
                                        .map(topic -> topic.getName())
                                        .collect(Collectors.joining(", ")));
                }
            }
            
            // 3. Company Relevance Scoring (weight: +5 per relevant company)
            // Get companies from batch's company mappings
            List<Company> companies = batch.getCompanyMappings().stream()
                .map(BatchCompanyMapping::getCompany)
                .collect(Collectors.toList());
                
            int relevantCompanies = 0;
            
            for (Company company : companies) {
                // Company doesn't have getDomains() - use domain instead (singular)
                String domain = company.getDomain();
                boolean domainMatch = student.getSkills().stream()
                    .anyMatch(studentSkill -> 
                        domain.toLowerCase().contains(studentSkill.getSkill().getName().toLowerCase()));
                
                if (domainMatch) {
                    relevantCompanies++;
                    
                    // Company doesn't have isCurrentlyHiring() - check if hiring type is not null
                    if (company.getHiringType() != null) {
                        companyRelevanceScore += 2;
                    }
                }
            }
            
            // Base company score: 5 points per relevant company
            companyRelevanceScore += relevantCompanies * 5;
            
            if (relevantCompanies > 0) {
                matchReasons.add(relevantCompanies + " companies aligned with your skills are associated with this batch");
                
                // Add hiring opportunity information - companies with hiring type set
                long hiringCompanies = companies.stream()
                    .filter(company -> company.getHiringType() != null)
                    .count();
                
                if (hiringCompanies > 0) {
                    matchReasons.add(hiringCompanies + " companies are currently hiring for similar roles");
                }
            }
            
            // Calculate total score
            int totalScore = skillMatchScore + syllabusOverlapScore + companyRelevanceScore;
            
            // Only recommend if there's at least some match
            if (totalScore > 0) {
                // Create DTO without using builder pattern
                BatchRecommendationDto recommendationDto = new BatchRecommendationDto();
                recommendationDto.setBatchId(batch.getId());
                recommendationDto.setBatchName(batch.getName());
                recommendationDto.setDescription(batch.getDescription());
                recommendationDto.setDurationWeeks(batch.getDurationWeeks());
                recommendationDto.setSkillMatchScore(skillMatchScore);
                recommendationDto.setSyllabusOverlapScore(syllabusOverlapScore);
                recommendationDto.setCompanyRelevanceScore(companyRelevanceScore);
                recommendationDto.setTotalScore((double) totalScore);
                recommendationDto.setMatchReasons(matchReasons);
                
                // Get trainer name from batch trainers list
                String trainerName = "Not assigned yet";
                if (batch.getTrainers() != null && !batch.getTrainers().isEmpty()) {
                    BatchTrainer batchTrainer = batch.getTrainers().get(0);
                    if (batchTrainer != null && batchTrainer.getTrainer() != null) {
                        trainerName = batchTrainer.getTrainer().getName();
                    }
                }
                recommendationDto.setTrainerName(trainerName);
                
                // Batch doesn't have getStartDate() - use a default value
                recommendationDto.setStartDate("To be announced");
                
                recommendations.add(recommendationDto);
            }
        }
        
        // Sort by total score (descending)
        recommendations.sort(Comparator.comparing(BatchRecommendationDto::getTotalScore).reversed());
        
        // Return top recommendations (limiting to 5)
        return recommendations.stream()
            .limit(5)
            .collect(Collectors.toList());
    }
    
    // Helper method to extract batch technologies
    private Set<String> getBatchTechnologies(Batch batch) {
        Set<String> technologies = new HashSet<>();
        
        if (batch.getSyllabus() != null) {
            // Add technologies from syllabus topics
            batch.getSyllabus().getTopics().forEach(topic -> {
                technologies.add(topic.getName().toLowerCase());
                // Assuming SyllabusTopic has getTechnologies() method
                if (topic.getTechnologies() != null) {
                    Arrays.stream(topic.getTechnologies().split(","))
                        .map(String::toLowerCase)
                        .map(String::trim)
                        .forEach(technologies::add);
                }
            });
        }
        
        return technologies;
    }
    
    // Helper method to get topics student already knows
    private Set<String> getStudentKnownTopics(Student student) {
        Set<String> knownTopics = new HashSet<>();
        
        // Add topics from skills - fix studentSkill access
        student.getSkills().forEach(studentSkill -> 
            knownTopics.add(studentSkill.getSkill().getName().toLowerCase()));
        
        // Add topics from previous batches - assuming getBatchHistory method exists
        student.getBatchHistory().forEach(history -> {
            if (history.getBatch() != null && 
                history.getBatch().getSyllabus() != null) {
                history.getBatch().getSyllabus().getTopics()
                    .forEach(topic -> knownTopics.add(topic.getName().toLowerCase()));
            }
        });
        
        return knownTopics;
    }
}