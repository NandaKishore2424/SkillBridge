package com.college.skillbridge.repositories;

import com.college.skillbridge.enums.BatchStatus;
import com.college.skillbridge.models.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID> {
    List<Batch> findByStatusIn(List<BatchStatus> statuses);
}