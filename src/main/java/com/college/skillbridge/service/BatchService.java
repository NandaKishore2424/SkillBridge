package com.college.skillbridge.service;

import com.college.skillbridge.enums.BatchStatus;
import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.Company;
import com.college.skillbridge.models.Trainer;

import java.util.List;
import java.util.UUID;

public interface BatchService {
    List<Batch> getAllBatches();
    List<Batch> getBatchesByStatus(BatchStatus status);
    void assignTrainerToBatch(UUID trainerId, UUID batchId);
    List<Trainer> getTrainersForBatch(UUID batchId);
    List<Company> getCompaniesMappedToBatch(UUID batchId);
}