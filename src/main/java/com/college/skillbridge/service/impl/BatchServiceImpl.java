package com.college.skillbridge.service.impl;

import com.college.skillbridge.enums.BatchStatus;
import com.college.skillbridge.models.*;
import com.college.skillbridge.repositories.*;
import com.college.skillbridge.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchRepository batchRepository;
    
    @Autowired
    private TrainerRepository trainerRepository;
    
    @Autowired
    private BatchTrainerRepository batchTrainerRepository;
    
    @Autowired
    private BatchCompanyMappingRepository batchCompanyMappingRepository;
    
    @Autowired
    private StudentBatchHistoryRepository studentBatchHistoryRepository;

    @Override
    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    @Override
    public List<Batch> getBatchesByStatus(BatchStatus status) {
        // Get all batches that have at least one student with the specified status
        List<StudentBatchHistory> histories = studentBatchHistoryRepository.findAll().stream()
                .filter(history -> history.getStatus() == status)
                .collect(Collectors.toList());
        
        return histories.stream()
                .map(StudentBatchHistory::getBatch)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public void assignTrainerToBatch(UUID trainerId, UUID batchId) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found with id: " + trainerId));
        
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + batchId));
        
        BatchTrainer batchTrainer = new BatchTrainer();
        batchTrainer.setTrainer(trainer);
        batchTrainer.setBatch(batch);
        batchTrainer.setRoleDescription("Main Instructor");
        
        batchTrainerRepository.save(batchTrainer);
    }

    @Override
    public List<Trainer> getTrainersForBatch(UUID batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + batchId));
        
        return batchTrainerRepository.findByBatch(batch).stream()
                .map(BatchTrainer::getTrainer)
                .collect(Collectors.toList());
    }

    @Override
    public List<Company> getCompaniesMappedToBatch(UUID batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + batchId));
        
        return batchCompanyMappingRepository.findByBatch(batch).stream()
                .map(BatchCompanyMapping::getCompany)
                .collect(Collectors.toList());
    }
}