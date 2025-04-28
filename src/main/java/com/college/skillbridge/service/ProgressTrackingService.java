package com.college.skillbridge.service;

import com.college.skillbridge.dto.ProgressUpdateDto;
import com.college.skillbridge.models.TrainingProgress;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProgressTrackingService {
    TrainingProgress updateStudentProgress(ProgressUpdateDto progressUpdateDto, UUID trainerId);
    List<TrainingProgress> getStudentProgress(UUID studentId, UUID batchId);
    Double getBatchProgressPercentage(UUID batchId);
    Map<String, Object> getBatchPerformanceSummary(UUID batchId);
    List<Map<String, Object>> getTopPerformingStudents(UUID batchId, int limit);
}