package com.college.skillbridge.integration;

import com.college.skillbridge.models.Batch;
import com.college.skillbridge.repositories.BatchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BatchIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private BatchRepository batchRepository;
    
    @Test
    public void testGetAllBatches() throws Exception {
        // Create test batch
        Batch batch = new Batch();
        batch.setName("Test Batch");
        batchRepository.save(batch);
        
        // Perform API call and verify
        mockMvc.perform(get("/api/v1/batches")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }
}
