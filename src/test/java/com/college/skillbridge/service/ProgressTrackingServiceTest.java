package com.college.skillbridge.service;

import com.college.skillbridge.dto.ProgressUpdateDto;
import com.college.skillbridge.enums.ProgressStatus;
import com.college.skillbridge.models.*;
import com.college.skillbridge.repositories.BatchRepository;
import com.college.skillbridge.repositories.StudentRepository;
import com.college.skillbridge.repositories.SyllabusTopicRepository;
import com.college.skillbridge.repositories.TrainingProgressRepository;
import com.college.skillbridge.repositories.TrainerRepository;
import com.college.skillbridge.service.impl.ProgressTrackingServiceImpl;
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
public class ProgressTrackingServiceTest {

    @Mock
    private TrainingProgressRepository progressRepository;
    
    @Mock
    private StudentRepository studentRepository;
    
    @Mock
    private BatchRepository batchRepository;
    
    @Mock
    private SyllabusTopicRepository topicRepository;
    
    @Mock
    private TrainerRepository trainerRepository;
    
    @InjectMocks
    private ProgressTrackingServiceImpl progressTrackingService;
    
    @Captor
    private ArgumentCaptor<TrainingProgress> progressCaptor;
    
    private Student testStudent;
    private Batch testBatch;
    private SyllabusTopic testTopic;
    private Trainer testTrainer;
    private UUID studentId, batchId, topicId, trainerId;
    
    @BeforeEach
    void setUp() {
        // Create test entities
        studentId = UUID.randomUUID();
        testStudent = new Student();
        testStudent.setId(studentId);
        testStudent.setName("Test Student");
        
        batchId = UUID.randomUUID();
        testBatch = new Batch();
        testBatch.setId(batchId);
        testBatch.setName("Test Batch");
        
        topicId = UUID.randomUUID();
        testTopic = new SyllabusTopic();
        testTopic.setId(topicId);
        testTopic.setName("Java Basics");
        
        trainerId = UUID.randomUUID();
        testTrainer = new Trainer();
        testTrainer.setId(trainerId);
        testTrainer.setName("Test Trainer");
        
        // Mock repository responses
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
        when(batchRepository.findById(batchId)).thenReturn(Optional.of(testBatch));
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(testTopic));
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(testTrainer));
    }
    
    @Test
    void shouldUpdateStudentProgress() {
        // Given
        ProgressUpdateDto updateDto = new ProgressUpdateDto();
        updateDto.setStudentId(studentId);
        updateDto.setBatchId(batchId);
        updateDto.setTopicId(topicId);
        updateDto.setStatus(ProgressStatus.COMPLETED);
        updateDto.setFeedback("Great work on understanding Java basics!");
        
        TrainingProgress savedProgress = new TrainingProgress();
        savedProgress.setId(UUID.randomUUID());
        savedProgress.setStudent(testStudent);
        savedProgress.setBatch(testBatch);
        savedProgress.setTopic(testTopic);
        savedProgress.setStatus(ProgressStatus.COMPLETED);
        savedProgress.setFeedback(updateDto.getFeedback());
        savedProgress.setUpdatedBy(testTrainer);
        
        when(progressRepository.findByStudentAndBatchAndTopic(testStudent, testBatch, testTopic))
            .thenReturn(Optional.empty());
        when(progressRepository.save(any(TrainingProgress.class))).thenReturn(savedProgress);
        
        // When
        TrainingProgress result = progressTrackingService.updateStudentProgress(updateDto, trainerId);
        
        // Then
        verify(progressRepository).save(progressCaptor.capture());
        TrainingProgress capturedProgress = progressCaptor.getValue();
        
        // Verify correct entities were set
        assertEquals(testStudent, capturedProgress.getStudent());
        assertEquals(testBatch, capturedProgress.getBatch());
        assertEquals(testTopic, capturedProgress.getTopic());
        assertEquals(testTrainer, capturedProgress.getUpdatedBy());
        
        // Verify correct data from DTO
        assertEquals(ProgressStatus.COMPLETED, capturedProgress.getStatus());
        assertEquals("Great work on understanding Java basics!", capturedProgress.getFeedback());
        
        // Verify timestamp was set
        assertNotNull(capturedProgress.getUpdatedAt());
        
        // Verify return value
        assertNotNull(result);
        assertEquals(savedProgress.getId(), result.getId());
    }
    
    @Test
    void shouldUpdateExistingProgress() {
        // Given
        ProgressUpdateDto updateDto = new ProgressUpdateDto();
        updateDto.setStudentId(studentId);
        updateDto.setBatchId(batchId);
        updateDto.setTopicId(topicId);
        updateDto.setStatus(ProgressStatus.IN_PROGRESS);
        updateDto.setFeedback("Making good progress!");
        
        TrainingProgress existingProgress = new TrainingProgress();
        existingProgress.setId(UUID.randomUUID());
        existingProgress.setStudent(testStudent);
        existingProgress.setBatch(testBatch);
        existingProgress.setTopic(testTopic);
        existingProgress.setStatus(ProgressStatus.PENDING);
        existingProgress.setFeedback(null);
        existingProgress.setUpdatedAt(LocalDateTime.now().minusDays(1));
        
        when(progressRepository.findByStudentAndBatchAndTopic(testStudent, testBatch, testTopic))
            .thenReturn(Optional.of(existingProgress));
        when(progressRepository.save(any(TrainingProgress.class))).thenAnswer(i -> i.getArguments()[0]);
        
        // When
        TrainingProgress result = progressTrackingService.updateStudentProgress(updateDto, trainerId);
        
        // Then
        verify(progressRepository).save(progressCaptor.capture());
        TrainingProgress capturedProgress = progressCaptor.getValue();
        
        // Verify existing ID was maintained
        assertEquals(existingProgress.getId(), capturedProgress.getId());
        
        // Verify status and feedback were updated
        assertEquals(ProgressStatus.IN_PROGRESS, capturedProgress.getStatus());
        assertEquals("Making good progress!", capturedProgress.getFeedback());
        
        // Verify timestamp was updated
        assertNotEquals(existingProgress.getUpdatedAt(), capturedProgress.getUpdatedAt());
    }
    
    @Test
    void shouldGetBatchProgressPercentage() {
        // Given
        when(progressRepository.getAverageBatchProgress(batchId)).thenReturn(75.0);
        
        // When
        Double progressPercentage = progressTrackingService.getBatchProgressPercentage(batchId);
        
        // Then
        assertEquals(75.0, progressPercentage);
    }
    
    @Test
    void shouldHandleNullBatchProgressPercentage() {
        // Given
        when(progressRepository.getAverageBatchProgress(batchId)).thenReturn(null);
        
        // When
        Double progressPercentage = progressTrackingService.getBatchProgressPercentage(batchId);
        
        // Then
        assertEquals(0.0, progressPercentage);
    }
}