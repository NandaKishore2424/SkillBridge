package com.college.skillbridge.service.impl;

import com.college.skillbridge.dto.StudentFeedbackDto;
import com.college.skillbridge.dto.TrainerFeedbackDto;
import com.college.skillbridge.models.*;
import com.college.skillbridge.repositories.*;
import com.college.skillbridge.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final TrainerFeedbackRepository trainerFeedbackRepository;
    private final StudentFeedbackRepository studentFeedbackRepository;
    private final TrainerRepository trainerRepository;
    private final StudentRepository studentRepository;
    private final BatchRepository batchRepository;

    @Override
    public TrainerFeedback addTrainerFeedback(UUID trainerId, TrainerFeedbackDto feedbackDto) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
                
        Student student = studentRepository.findById(feedbackDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
                
        Batch batch = batchRepository.findById(feedbackDto.getBatchId())
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        
        TrainerFeedback feedback = new TrainerFeedback();
        feedback.setTrainer(trainer);
        feedback.setStudent(student);
        feedback.setBatch(batch);
        feedback.setContent(feedbackDto.getContent());
        feedback.setRating(feedbackDto.getRating());
        feedback.setTimestamp(LocalDateTime.now());
        
        return trainerFeedbackRepository.save(feedback);
    }

    @Override
    public List<TrainerFeedback> getTrainerFeedbackForStudent(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
                
        return trainerFeedbackRepository.findByStudent(student);
    }

    @Override
    public List<TrainerFeedback> getTrainerFeedbackByBatch(UUID batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
                
        return trainerFeedbackRepository.findByBatch(batch);
    }

    @Override
    public Double getAverageStudentRating(UUID studentId) {
        return trainerFeedbackRepository.getAverageStudentRating(studentId);
    }

    @Override
    public StudentFeedback addStudentFeedback(UUID studentId, StudentFeedbackDto feedbackDto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
                
        Trainer trainer = trainerRepository.findById(feedbackDto.getTrainerId())
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
                
        Batch batch = batchRepository.findById(feedbackDto.getBatchId())
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        
        StudentFeedback feedback = new StudentFeedback();
        feedback.setStudent(student);
        feedback.setTrainer(trainer);
        feedback.setBatch(batch);
        feedback.setComment(feedbackDto.getComment());
        feedback.setRating(feedbackDto.getRating());
        feedback.setTimestamp(LocalDateTime.now());
        
        return studentFeedbackRepository.save(feedback);
    }

    @Override
    public List<StudentFeedback> getStudentFeedbackForTrainer(UUID trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
                
        return studentFeedbackRepository.findByTrainer(trainer);
    }

    @Override
    public List<StudentFeedback> getStudentFeedbackByBatch(UUID batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
                
        return studentFeedbackRepository.findByBatch(batch);
    }

    @Override
    public Double getAverageTrainerRating(UUID trainerId) {
        return studentFeedbackRepository.getAverageTrainerRating(trainerId);
    }

    @Override
    public List<Map<String, Object>> getTopRatedTrainers(int limit) {
        List<Trainer> trainers = trainerRepository.findAll();
        
        List<Map<String, Object>> results = trainers.stream()
            .map(trainer -> {
                Double rating = getAverageTrainerRating(trainer.getId());
                if (rating == null) rating = 0.0;
                
                Map<String, Object> trainerRating = new HashMap<>();
                trainerRating.put("id", trainer.getId());
                trainerRating.put("name", trainer.getName());
                trainerRating.put("specialization", trainer.getSpecialization());
                trainerRating.put("averageRating", rating);
                trainerRating.put("feedbackCount", studentFeedbackRepository.findByTrainer(trainer).size());
                
                return trainerRating;
            })
            .sorted((t1, t2) -> Double.compare((Double) t2.get("averageRating"), (Double) t1.get("averageRating")))
            .limit(limit)
            .collect(Collectors.toList());
            
        return results;
    }

    @Override
    public Map<String, Object> getFeedbackSummaryForBatch(UUID batchId) {
        Map<String, Object> summary = new HashMap<>();
        
        Double trainerRatingAvg = studentFeedbackRepository.getAverageBatchRating(batchId);
        Double studentRatingAvg = trainerFeedbackRepository.getAverageBatchRating(batchId);
        
        List<StudentFeedback> studentFeedbacks = studentFeedbackRepository.findByBatch(
                batchRepository.findById(batchId).orElseThrow(() -> new RuntimeException("Batch not found"))
        );
        
        List<TrainerFeedback> trainerFeedbacks = trainerFeedbackRepository.findByBatch(
                batchRepository.findById(batchId).orElseThrow(() -> new RuntimeException("Batch not found"))
        );
        
        summary.put("batchId", batchId);
        summary.put("averageTrainerRating", trainerRatingAvg != null ? trainerRatingAvg : 0.0);
        summary.put("averageStudentRating", studentRatingAvg != null ? studentRatingAvg : 0.0);
        summary.put("studentFeedbackCount", studentFeedbacks.size());
        summary.put("trainerFeedbackCount", trainerFeedbacks.size());
        
        return summary;
    }
}