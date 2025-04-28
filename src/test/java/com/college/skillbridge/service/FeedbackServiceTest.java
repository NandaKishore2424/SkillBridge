package com.college.skillbridge.service;

import com.college.skillbridge.dto.StudentFeedbackDto;
import com.college.skillbridge.dto.TrainerFeedbackDto;
import com.college.skillbridge.models.*;
import com.college.skillbridge.repositories.*;
import com.college.skillbridge.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

    @Mock
    private TrainerFeedbackRepository trainerFeedbackRepository;
    
    @Mock
    private StudentFeedbackRepository studentFeedbackRepository;
    
    @Mock
    private TrainerRepository trainerRepository;
    
    @Mock
    private StudentRepository studentRepository;
    
    @Mock
    private BatchRepository batchRepository;
    
    @InjectMocks
    private FeedbackServiceImpl feedbackService;
    
    @Captor
    private ArgumentCaptor<TrainerFeedback> trainerFeedbackCaptor;
    
    @Captor
    private ArgumentCaptor<StudentFeedback> studentFeedbackCaptor;
    
    private Student testStudent;
    private Trainer testTrainer;
    private Batch testBatch;
    private UUID studentId, trainerId, batchId;
    
    @BeforeEach
    void setUp() {
        // Create test entities
        studentId = UUID.randomUUID();
        testStudent = new Student();
        testStudent.setId(studentId);
        testStudent.setName("Test Student");
        testStudent.setEmail("student@example.com");
        
        trainerId = UUID.randomUUID();
        testTrainer = new Trainer();
        testTrainer.setId(trainerId);
        testTrainer.setName("Test Trainer");
        testTrainer.setSpecialization("Java Development");
        testTrainer.setEmail("trainer@example.com");
        
        batchId = UUID.randomUUID();
        testBatch = new Batch();
        testBatch.setId(batchId);
        testBatch.setName("Java Fullstack 2025");
        
        // Mock repository responses
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(testTrainer));
        when(batchRepository.findById(batchId)).thenReturn(Optional.of(testBatch));
    }
    
    @Test
    void shouldAddTrainerFeedback() {
        // Given
        TrainerFeedbackDto feedbackDto = new TrainerFeedbackDto();
        feedbackDto.setStudentId(studentId);
        feedbackDto.setBatchId(batchId);
        feedbackDto.setContent("Excellent progress in Java concepts. Keep it up!");
        feedbackDto.setRating(5);
        
        TrainerFeedback savedFeedback = new TrainerFeedback();
        savedFeedback.setId(UUID.randomUUID());
        savedFeedback.setTrainer(testTrainer);
        savedFeedback.setStudent(testStudent);
        savedFeedback.setBatch(testBatch);
        savedFeedback.setContent(feedbackDto.getContent());
        savedFeedback.setRating(feedbackDto.getRating());
        savedFeedback.setTimestamp(LocalDateTime.now());
        
        when(trainerFeedbackRepository.save(any(TrainerFeedback.class))).thenReturn(savedFeedback);
        
        // When
        TrainerFeedback result = feedbackService.addTrainerFeedback(trainerId, feedbackDto);
        
        // Then
        verify(trainerFeedbackRepository).save(trainerFeedbackCaptor.capture());
        TrainerFeedback capturedFeedback = trainerFeedbackCaptor.getValue();
        
        // Verify correct entities were set
        assertEquals(testTrainer, capturedFeedback.getTrainer());
        assertEquals(testStudent, capturedFeedback.getStudent());
        assertEquals(testBatch, capturedFeedback.getBatch());
        
        // Verify data from DTO was set correctly
        assertEquals(feedbackDto.getContent(), capturedFeedback.getContent());
        assertEquals(feedbackDto.getRating(), capturedFeedback.getRating());
        
        // Verify timestamp was set
        assertNotNull(capturedFeedback.getTimestamp());
        
        // Verify result is not null and matches expected
        assertNotNull(result);
        assertEquals(savedFeedback.getId(), result.getId());
    }
    
    @Test
    void shouldGetTrainerFeedbackForStudent() {
        // Given
        List<TrainerFeedback> expectedFeedbacks = new ArrayList<>();
        
        TrainerFeedback feedback1 = new TrainerFeedback();
        feedback1.setId(UUID.randomUUID());
        feedback1.setTrainer(testTrainer);
        feedback1.setStudent(testStudent);
        feedback1.setContent("Good work on assignment");
        feedback1.setRating(4);
        expectedFeedbacks.add(feedback1);
        
        TrainerFeedback feedback2 = new TrainerFeedback();
        feedback2.setId(UUID.randomUUID());
        feedback2.setTrainer(testTrainer);
        feedback2.setStudent(testStudent);
        feedback2.setContent("Excellent understanding of concepts");
        feedback2.setRating(5);
        expectedFeedbacks.add(feedback2);
        
        when(trainerFeedbackRepository.findByStudent(testStudent)).thenReturn(expectedFeedbacks);
        
        // When
        List<TrainerFeedback> result = feedbackService.getTrainerFeedbackForStudent(studentId);
        
        // Then
        assertEquals(2, result.size());
        assertEquals(expectedFeedbacks, result);
    }
    
    @Test
    void shouldGetTrainerFeedbackByBatch() {
        // Given
        List<TrainerFeedback> expectedFeedbacks = new ArrayList<>();
        
        TrainerFeedback feedback1 = new TrainerFeedback();
        feedback1.setId(UUID.randomUUID());
        feedback1.setBatch(testBatch);
        expectedFeedbacks.add(feedback1);
        
        when(trainerFeedbackRepository.findByBatch(testBatch)).thenReturn(expectedFeedbacks);
        
        // When
        List<TrainerFeedback> result = feedbackService.getTrainerFeedbackByBatch(batchId);
        
        // Then
        assertEquals(expectedFeedbacks, result);
    }
    
    @Test
    void shouldGetAverageStudentRating() {
        // Given
        Double expectedRating = 4.5;
        when(trainerFeedbackRepository.getAverageStudentRating(studentId)).thenReturn(expectedRating);
        
        // When
        Double result = feedbackService.getAverageStudentRating(studentId);
        
        // Then
        assertEquals(expectedRating, result);
    }
    
    @Test
    void shouldHandleNullAverageStudentRating() {
        // Given
        when(trainerFeedbackRepository.getAverageStudentRating(studentId)).thenReturn(null);
        
        // When
        Double result = feedbackService.getAverageStudentRating(studentId);
        
        // Then
        assertNull(result);
    }
    
    @Test
    void shouldAddStudentFeedback() {
        // Given
        StudentFeedbackDto feedbackDto = new StudentFeedbackDto();
        feedbackDto.setTrainerId(trainerId);
        feedbackDto.setBatchId(batchId);
        feedbackDto.setComment("Great teaching methods and clear explanations.");
        feedbackDto.setRating(4);
        
        StudentFeedback savedFeedback = new StudentFeedback();
        savedFeedback.setId(UUID.randomUUID());
        savedFeedback.setStudent(testStudent);
        savedFeedback.setTrainer(testTrainer);
        savedFeedback.setBatch(testBatch);
        savedFeedback.setComment(feedbackDto.getComment());
        savedFeedback.setRating(feedbackDto.getRating());
        savedFeedback.setTimestamp(LocalDateTime.now());
        
        when(studentFeedbackRepository.save(any(StudentFeedback.class))).thenReturn(savedFeedback);
        
        // When
        StudentFeedback result = feedbackService.addStudentFeedback(studentId, feedbackDto);
        
        // Then
        verify(studentFeedbackRepository).save(studentFeedbackCaptor.capture());
        StudentFeedback capturedFeedback = studentFeedbackCaptor.getValue();
        
        // Verify correct entities were set
        assertEquals(testStudent, capturedFeedback.getStudent());
        assertEquals(testTrainer, capturedFeedback.getTrainer());
        assertEquals(testBatch, capturedFeedback.getBatch());
        
        // Verify data from DTO was set correctly
        assertEquals(feedbackDto.getComment(), capturedFeedback.getComment());
        assertEquals(feedbackDto.getRating(), capturedFeedback.getRating());
        
        // Verify timestamp was set
        assertNotNull(capturedFeedback.getTimestamp());
        
        // Verify result
        assertNotNull(result);
        assertEquals(savedFeedback.getId(), result.getId());
    }
    
    @Test
    void shouldGetStudentFeedbackForTrainer() {
        // Given
        List<StudentFeedback> expectedFeedbacks = new ArrayList<>();
        
        StudentFeedback feedback1 = new StudentFeedback();
        feedback1.setId(UUID.randomUUID());
        feedback1.setTrainer(testTrainer);
        feedback1.setStudent(testStudent);
        expectedFeedbacks.add(feedback1);
        
        when(studentFeedbackRepository.findByTrainer(testTrainer)).thenReturn(expectedFeedbacks);
        
        // When
        List<StudentFeedback> result = feedbackService.getStudentFeedbackForTrainer(trainerId);
        
        // Then
        assertEquals(expectedFeedbacks, result);
    }
    
    @Test
    void shouldGetAverageTrainerRating() {
        // Given
        Double expectedRating = 4.8;
        when(studentFeedbackRepository.getAverageTrainerRating(trainerId)).thenReturn(expectedRating);
        
        // When
        Double result = feedbackService.getAverageTrainerRating(trainerId);
        
        // Then
        assertEquals(expectedRating, result);
    }
    
    @Test
    void shouldGetTopRatedTrainers() {
        // Given
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(testTrainer);
        
        Trainer anotherTrainer = new Trainer();
        anotherTrainer.setId(UUID.randomUUID());
        anotherTrainer.setName("Another Trainer");
        anotherTrainer.setSpecialization("Web Development");
        trainers.add(anotherTrainer);
        
        when(trainerRepository.findAll()).thenReturn(trainers);
        when(studentFeedbackRepository.getAverageTrainerRating(trainerId)).thenReturn(4.8);
        when(studentFeedbackRepository.getAverageTrainerRating(anotherTrainer.getId())).thenReturn(4.2);
        
        when(studentFeedbackRepository.findByTrainer(testTrainer)).thenReturn(Collections.singletonList(new StudentFeedback()));
        when(studentFeedbackRepository.findByTrainer(anotherTrainer)).thenReturn(Arrays.asList(new StudentFeedback(), new StudentFeedback()));
        
        // When
        List<Map<String, Object>> result = feedbackService.getTopRatedTrainers(2);
        
        // Then
        assertEquals(2, result.size());
        
        // First trainer should be the test trainer (higher rating)
        assertEquals(testTrainer.getId(), result.get(0).get("id"));
        assertEquals(4.8, result.get(0).get("averageRating"));
        assertEquals(1, result.get(0).get("feedbackCount"));
        
        // Second trainer should be the other trainer
        assertEquals(anotherTrainer.getId(), result.get(1).get("id"));
        assertEquals(4.2, result.get(1).get("averageRating"));
        assertEquals(2, result.get(1).get("feedbackCount"));
    }
    
    @Test
    void shouldHandleNoFeedbackForTopRatedTrainers() {
        // Given
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(testTrainer);
        
        when(trainerRepository.findAll()).thenReturn(trainers);
        when(studentFeedbackRepository.getAverageTrainerRating(trainerId)).thenReturn(null);
        when(studentFeedbackRepository.findByTrainer(testTrainer)).thenReturn(Collections.emptyList());
        
        // When
        List<Map<String, Object>> result = feedbackService.getTopRatedTrainers(1);
        
        // Then
        assertEquals(1, result.size());
        assertEquals(testTrainer.getId(), result.get(0).get("id"));
        assertEquals(0.0, result.get(0).get("averageRating"));
        assertEquals(0, result.get(0).get("feedbackCount"));
    }
    
    @Test
    void shouldGetFeedbackSummaryForBatch() {
        // Given
        Double trainerRatingAvg = 4.5;
        Double studentRatingAvg = 4.2;
        
        List<StudentFeedback> studentFeedbacks = Arrays.asList(
            new StudentFeedback(), new StudentFeedback()
        );
        
        List<TrainerFeedback> trainerFeedbacks = Collections.singletonList(
            new TrainerFeedback()
        );
        
        when(studentFeedbackRepository.getAverageBatchRating(batchId)).thenReturn(trainerRatingAvg);
        when(trainerFeedbackRepository.getAverageBatchRating(batchId)).thenReturn(studentRatingAvg);
        when(studentFeedbackRepository.findByBatch(testBatch)).thenReturn(studentFeedbacks);
        when(trainerFeedbackRepository.findByBatch(testBatch)).thenReturn(trainerFeedbacks);
        
        // When
        Map<String, Object> result = feedbackService.getFeedbackSummaryForBatch(batchId);
        
        // Then
        assertEquals(batchId, result.get("batchId"));
        assertEquals(trainerRatingAvg, result.get("averageTrainerRating"));
        assertEquals(studentRatingAvg, result.get("averageStudentRating"));
        assertEquals(2, result.get("studentFeedbackCount"));
        assertEquals(1, result.get("trainerFeedbackCount"));
    }
    
    @Test
    void shouldHandleNullRatingsInFeedbackSummary() {
        // Given
        when(studentFeedbackRepository.getAverageBatchRating(batchId)).thenReturn(null);
        when(trainerFeedbackRepository.getAverageBatchRating(batchId)).thenReturn(null);
        when(studentFeedbackRepository.findByBatch(testBatch)).thenReturn(Collections.emptyList());
        when(trainerFeedbackRepository.findByBatch(testBatch)).thenReturn(Collections.emptyList());
        
        // When
        Map<String, Object> result = feedbackService.getFeedbackSummaryForBatch(batchId);
        
        // Then
        assertEquals(0.0, result.get("averageTrainerRating"));
        assertEquals(0.0, result.get("averageStudentRating"));
        assertEquals(0, result.get("studentFeedbackCount"));
        assertEquals(0, result.get("trainerFeedbackCount"));
    }
}