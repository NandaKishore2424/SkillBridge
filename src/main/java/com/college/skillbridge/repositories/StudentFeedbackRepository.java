package com.college.skillbridge.repositories;

import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.Student;
import com.college.skillbridge.models.StudentFeedback;
import com.college.skillbridge.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentFeedbackRepository extends JpaRepository<StudentFeedback, UUID> {
    List<StudentFeedback> findByStudent(Student student);
    List<StudentFeedback> findByTrainer(Trainer trainer);
    List<StudentFeedback> findByBatch(Batch batch);
    List<StudentFeedback> findByTrainerAndBatch(Trainer trainer, Batch batch);
    
    @Query("SELECT AVG(sf.rating) FROM StudentFeedback sf WHERE sf.trainer.id = :trainerId")
    Double getAverageTrainerRating(UUID trainerId);
    
    @Query("SELECT AVG(sf.rating) FROM StudentFeedback sf WHERE sf.batch.id = :batchId")
    Double getAverageBatchRating(UUID batchId);
}