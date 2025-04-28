package com.college.skillbridge.config;

import com.college.skillbridge.enums.HiringType;
import com.college.skillbridge.enums.SkillLevel;
import com.college.skillbridge.models.*;
import com.college.skillbridge.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private BatchRepository batchRepository;
    
    @Autowired
    private SyllabusRepository syllabusRepository;
    
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TrainerRepository trainerRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    // Will be configured in security config
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Only seed if database is empty
        if (studentRepository.count() == 0) {
            seedSkills();
            seedTrainers();
            seedCompanies();
            seedAdmin();
            seedSyllabusAndBatches();
            seedStudents();
        }
    }
    
    private void seedSkills() {
        Arrays.asList("Java", "Python", "JavaScript", "React", "Angular", "Spring Boot", 
                      "Node.js", "SQL", "MongoDB", "AWS", "Docker", "DSA").forEach(name -> {
            Skill skill = new Skill();
            skill.setName(name);
            skillRepository.save(skill);
        });
        
        System.out.println("Skills seeded successfully!");
    }
    
    private void seedTrainers() {
        Trainer trainer1 = new Trainer();
        trainer1.setName("John Smith");
        trainer1.setEmail("john.smith@example.com");
        trainer1.setPassword(passwordEncoder.encode("password"));
        trainer1.setSpecialization("Full Stack Development");
        trainer1.setBio("10+ years of experience in industry and education");
        trainerRepository.save(trainer1);
        
        Trainer trainer2 = new Trainer();
        trainer2.setName("Emily Johnson");
        trainer2.setEmail("emily.johnson@example.com");
        trainer2.setPassword(passwordEncoder.encode("password"));
        trainer2.setSpecialization("Data Structures & Algorithms");
        trainer2.setBio("Former tech lead with expertise in algorithm optimization");
        trainerRepository.save(trainer2);
        
        System.out.println("Trainers seeded successfully!");
    }
    
    private void seedCompanies() {
        Company company1 = new Company();
        company1.setName("TechCorp");
        company1.setDomain("FinTech");
        company1.setHiringType(HiringType.FULL_TIME);
        company1.setNotes("Looking for strong problem-solving skills");
        companyRepository.save(company1);
        
        Company company2 = new Company();
        company2.setName("WebSolutions");
        company2.setDomain("EdTech");
        company2.setHiringType(HiringType.BOTH);
        company2.setNotes("Focuses on full stack development skills");
        companyRepository.save(company2);
        
        System.out.println("Companies seeded successfully!");
    }
    
    private void seedAdmin() {
        Admin admin = new Admin();
        admin.setName("Admin User");
        admin.setEmail("admin@skillbridge.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        adminRepository.save(admin);
        
        System.out.println("Admin seeded successfully!");
    }
    
    private void seedSyllabusAndBatches() {
        // First syllabus and batch
        Syllabus dsa = new Syllabus();
        dsa.setTitle("DSA Fundamentals");
        dsa.setDescription("Comprehensive course covering data structures and algorithms");
        syllabusRepository.save(dsa);
        
        Batch dsaBatch = new Batch();
        dsaBatch.setName("DSA Batch 2025");
        dsaBatch.setDescription("Prepare students for technical interviews");
        dsaBatch.setDurationWeeks(8);
        dsaBatch.setSyllabus(dsa);
        batchRepository.save(dsaBatch);
        
        // Second syllabus and batch
        Syllabus fullStack = new Syllabus();
        fullStack.setTitle("Full Stack Development");
        fullStack.setDescription("End-to-end web development with modern technologies");
        syllabusRepository.save(fullStack);
        
        Batch fullStackBatch = new Batch();
        fullStackBatch.setName("Full Stack Batch 2025");
        fullStackBatch.setDescription("Hands-on web development training");
        fullStackBatch.setDurationWeeks(12);
        fullStackBatch.setSyllabus(fullStack);
        batchRepository.save(fullStackBatch);
        
        System.out.println("Syllabi and Batches seeded successfully!");
    }
    
    private void seedStudents() {
        Student student = new Student();
        student.setName("Test Student");
        student.setEmail("student@test.com");
        student.setPassword(passwordEncoder.encode("password"));
        student.setYear(3);
        student.setDepartment("CSE");
        student.setCgpa(8.5f);
        student.setProblemSolvedCount(120);
        student.setGithubLink("https://github.com/teststudent");
        student.setPortfolioLink("https://teststudent.dev");
        studentRepository.save(student);
        
        System.out.println("Students seeded successfully!");
    }
}