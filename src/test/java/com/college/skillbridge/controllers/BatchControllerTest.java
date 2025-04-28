package com.college.skillbridge.controllers;

import com.college.skillbridge.enums.BatchStatus;
import com.college.skillbridge.models.Batch;
import com.college.skillbridge.service.BatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BatchController.class)
public class BatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BatchService batchService;

    @Test
    public void testGetAllBatches() throws Exception {
        // Create test data
        List<Batch> batches = Arrays.asList(new Batch(), new Batch());
        
        // Mock service behavior
        when(batchService.getAllBatches()).thenReturn(batches);
        
        // Perform API call and verify
        mockMvc.perform(get("/api/v1/batches"))
               .andExpect(status().isOk());
    }
    
    @Test
    public void testGetBatchesByStatus() throws Exception {
        // Create test data
        List<Batch> batches = Arrays.asList(new Batch(), new Batch());
        
        // Mock service behavior
        when(batchService.getBatchesByStatus(BatchStatus.ACTIVE)).thenReturn(batches);
        
        // Perform API call and verify
        mockMvc.perform(get("/api/v1/batches/by-status")
               .param("status", "ACTIVE"))
               .andExpect(status().isOk());
    }
}
