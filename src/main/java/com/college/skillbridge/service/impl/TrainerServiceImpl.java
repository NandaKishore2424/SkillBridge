package com.college.skillbridge.service.impl;

import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.BatchTrainer;
import com.college.skillbridge.models.Trainer;
import com.college.skillbridge.repositories.BatchTrainerRepository;
import com.college.skillbridge.repositories.TrainerRepository;
import com.college.skillbridge.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;
    
    @Autowired
    private BatchTrainerRepository batchTrainerRepository;

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    @Override
    public Trainer getTrainerById(UUID id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found with id: " + id));
    }

    @Override
    public List<Batch> getBatchesAssigned(UUID trainerId) {
        Trainer trainer = getTrainerById(trainerId);
        
        return batchTrainerRepository.findByTrainer(trainer).stream()
                .map(BatchTrainer::getBatch)
                .collect(Collectors.toList());
    }
}