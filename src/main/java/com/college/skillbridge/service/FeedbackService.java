package com.college.skillbridge.service;

import com.college.skillbridge.dto.StudentFeedbackDto;
import com.college.skillbridge.dto.TrainerFeedbackDto;
import com.college.skillbridge.models.StudentFeedback;
import com.college.skillbridge.models.TrainerFeedback;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FeedbackService {
    // Trainer providing feedback to students
    TrainerFeedback addTrainerFeedback(UUID trainerId, TrainerFeedbackDto feedbackDto);
    List<TrainerFeedback> getTrainerFeedbackForStudent(UUID studentId);
    List<TrainerFeedback> getTrainerFeedbackByBatch(UUID batchId);
    Double getAverageStudentRating(UUID studentId);
    
    // Students providing feedback to trainers
    StudentFeedback addStudentFeedback(UUID studentId, StudentFeedbackDto feedbackDto);
    List<StudentFeedback> getStudentFeedbackForTrainer(UUID trainerId);
    List<StudentFeedback> getStudentFeedbackByBatch(UUID batchId);
    Double getAverageTrainerRating(UUID trainerId);
    
    // Admin reports
    List<Map<String, Object>> getTopRatedTrainers(int limit);
    Map<String, Object> getFeedbackSummaryForBatch(UUID batchId);
}