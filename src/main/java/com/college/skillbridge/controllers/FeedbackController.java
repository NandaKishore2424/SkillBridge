package com.college.skillbridge.controllers;

import com.college.skillbridge.dto.StudentFeedbackDto;
import com.college.skillbridge.dto.TrainerFeedbackDto;
import com.college.skillbridge.models.StudentFeedback;
import com.college.skillbridge.models.TrainerFeedback;
import com.college.skillbridge.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
    
    // Trainer giving feedback to students
    @PostMapping("/trainer/{trainerId}")
    public ResponseEntity<TrainerFeedback> addTrainerFeedback(
            @PathVariable UUID trainerId, 
            @RequestBody TrainerFeedbackDto feedbackDto) {
        return ResponseEntity.ok(feedbackService.addTrainerFeedback(trainerId, feedbackDto));
    }
    
    @GetMapping("/trainer/for-student/{studentId}")
    public ResponseEntity<List<TrainerFeedback>> getTrainerFeedbackForStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(feedbackService.getTrainerFeedbackForStudent(studentId));
    }
    
    @GetMapping("/trainer/by-batch/{batchId}")
    public ResponseEntity<List<TrainerFeedback>> getTrainerFeedbackByBatch(@PathVariable UUID batchId) {
        return ResponseEntity.ok(feedbackService.getTrainerFeedbackByBatch(batchId));
    }
    
    @GetMapping("/student/average-rating/{studentId}")
    public ResponseEntity<Double> getAverageStudentRating(@PathVariable UUID studentId) {
        return ResponseEntity.ok(feedbackService.getAverageStudentRating(studentId));
    }
    
    // Student giving feedback to trainers
    @PostMapping("/student/{studentId}")
    public ResponseEntity<StudentFeedback> addStudentFeedback(
            @PathVariable UUID studentId, 
            @RequestBody StudentFeedbackDto feedbackDto) {
        return ResponseEntity.ok(feedbackService.addStudentFeedback(studentId, feedbackDto));
    }
    
    @GetMapping("/student/for-trainer/{trainerId}")
    public ResponseEntity<List<StudentFeedback>> getStudentFeedbackForTrainer(@PathVariable UUID trainerId) {
        return ResponseEntity.ok(feedbackService.getStudentFeedbackForTrainer(trainerId));
    }
    
    @GetMapping("/student/by-batch/{batchId}")
    public ResponseEntity<List<StudentFeedback>> getStudentFeedbackByBatch(@PathVariable UUID batchId) {
        return ResponseEntity.ok(feedbackService.getStudentFeedbackByBatch(batchId));
    }
    
    @GetMapping("/trainer/average-rating/{trainerId}")
    public ResponseEntity<Double> getAverageTrainerRating(@PathVariable UUID trainerId) {
        return ResponseEntity.ok(feedbackService.getAverageTrainerRating(trainerId));
    }
    
    // Admin reports
    @GetMapping("/top-trainers")
    public ResponseEntity<List<Map<String, Object>>> getTopRatedTrainers(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(feedbackService.getTopRatedTrainers(limit));
    }
    
    @GetMapping("/summary/batch/{batchId}")
    public ResponseEntity<Map<String, Object>> getFeedbackSummaryForBatch(@PathVariable UUID batchId) {
        return ResponseEntity.ok(feedbackService.getFeedbackSummaryForBatch(batchId));
    }
}