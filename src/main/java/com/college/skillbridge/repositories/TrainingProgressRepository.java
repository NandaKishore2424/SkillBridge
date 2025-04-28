package com.college.skillbridge.repositories;

import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.Student;
import com.college.skillbridge.models.SyllabusTopic;
import com.college.skillbridge.models.TrainingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingProgressRepository extends JpaRepository<TrainingProgress, UUID> {
    List<TrainingProgress> findByStudent(Student student);
    List<TrainingProgress> findByBatch(Batch batch);
    List<TrainingProgress> findByStudentAndBatch(Student student, Batch batch);
    Optional<TrainingProgress> findByStudentAndBatchAndTopic(Student student, Batch batch, SyllabusTopic topic);
    
    @Query("SELECT AVG(CASE WHEN tp.status = 'COMPLETED' THEN 100 WHEN tp.status = 'IN_PROGRESS' THEN 50 ELSE 0 END) " +
           "FROM TrainingProgress tp WHERE tp.batch.id = :batchId")
    Double getAverageBatchProgress(UUID batchId);
}