package com.college.skillbridge.controllers;

import com.college.skillbridge.dto.StudentRequestDto;
import com.college.skillbridge.dto.BatchRecommendationDto;
import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.Student;
import com.college.skillbridge.models.StudentBatchHistory;
import com.college.skillbridge.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody StudentRequestDto studentDto) {
        return ResponseEntity.ok(studentService.createStudent(studentDto));
    }

    @GetMapping("/by-skill")
    public ResponseEntity<List<Student>> findStudentsBySkill(@RequestParam String skill) {
        return ResponseEntity.ok(studentService.findStudentsBySkill(skill));
    }

    @PostMapping("/assign-batch/{id}")
    public ResponseEntity<Batch> assignStudentToBestFitBatch(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.assignStudentToBestFitBatch(id));
    }

    @GetMapping("/{id}/batch-history")
    public ResponseEntity<List<StudentBatchHistory>> getStudentBatchHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getStudentBatchHistory(id));
    }

    @GetMapping("/{id}/recommend-batches")
    public ResponseEntity<List<BatchRecommendationDto>> getRecommendedBatches(@PathVariable UUID id) {
        List<BatchRecommendationDto> recommendations = studentService.getRecommendedBatches(id);
        return ResponseEntity.ok(recommendations);
    }
}