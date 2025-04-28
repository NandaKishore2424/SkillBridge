package com.college.skillbridge.service;

import com.college.skillbridge.enums.BatchStatus;
import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.Trainer;
import com.college.skillbridge.repositories.BatchRepository;
import com.college.skillbridge.repositories.TrainerRepository;
import com.college.skillbridge.service.impl.BatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BatchServiceImplTest {

    @Mock
    private BatchRepository batchRepository;
    
    @Mock
    private TrainerRepository trainerRepository;
    
    @InjectMocks
    private BatchServiceImpl batchService;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testGetAllBatches() {
        // Create test data
        List<Batch> batches = Arrays.asList(new Batch(), new Batch());
        
        // Mock repository behavior
        when(batchRepository.findAll()).thenReturn(batches);
        
        // Call service method
        List<Batch> result = batchService.getAllBatches();
        
        // Verify result
        assertEquals(2, result.size());
        verify(batchRepository, times(1)).findAll();
    }
}
