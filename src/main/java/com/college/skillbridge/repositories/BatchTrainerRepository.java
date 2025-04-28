package com.college.skillbridge.repositories;

import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.BatchTrainer;
import com.college.skillbridge.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BatchTrainerRepository extends JpaRepository<BatchTrainer, UUID> {
    List<BatchTrainer> findByBatch(Batch batch);
    List<BatchTrainer> findByTrainer(Trainer trainer);
}