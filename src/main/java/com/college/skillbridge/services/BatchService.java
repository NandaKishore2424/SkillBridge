package com.college.skillbridge.services;

import com.college.skillbridge.dto.BatchDTO;
import com.college.skillbridge.dto.PaginatedResponse;
import com.college.skillbridge.models.Batch;
import com.college.skillbridge.repositories.BatchRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatchService {
    
    private final BatchRepository batchRepository;
    
    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }
    
    @Cacheable(value = "batches", key = "#id")
    @Transactional(readOnly = true)
    public BatchDTO getBatchById(Long id) {
        Batch batch = batchRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));
        return convertToDTO(batch);
    }
    
    @Cacheable(value = "batches", key = "'page_' + #pageNo + '_' + #pageSize")
    @Transactional(readOnly = true)
    public PaginatedResponse<BatchDTO> getAllBatches(int pageNo, int pageSize, String sortBy) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Batch> batchPage = batchRepository.findAll(pageRequest);
        Page<BatchDTO> dtoPage = batchPage.map(this::convertToDTO);
        return new PaginatedResponse<>(dtoPage);
    }
    
    // Other methods...
}