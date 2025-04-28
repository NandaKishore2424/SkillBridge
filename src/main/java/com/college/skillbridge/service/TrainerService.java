package com.college.skillbridge.service;

import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.Trainer;

import java.util.List;
import java.util.UUID;

public interface TrainerService {
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(UUID id);
    List<Batch> getBatchesAssigned(UUID trainerId);
}