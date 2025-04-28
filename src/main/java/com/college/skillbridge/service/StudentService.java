package com.college.skillbridge.service;

import com.college.skillbridge.dto.BatchRecommendationDto;
import com.college.skillbridge.dto.StudentRequestDto;
import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.Student;
import com.college.skillbridge.models.StudentBatchHistory;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(UUID id);
    Student createStudent(StudentRequestDto studentDto);
    List<Student> findStudentsBySkill(String skill);
    Batch assignStudentToBestFitBatch(UUID studentId);
    List<StudentBatchHistory> getStudentBatchHistory(UUID studentId);
    List<BatchRecommendationDto> getRecommendedBatches(UUID studentId);
}