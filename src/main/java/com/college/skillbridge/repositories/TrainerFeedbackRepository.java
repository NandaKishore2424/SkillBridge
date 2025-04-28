package com.college.skillbridge.repositories;

import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.Student;
import com.college.skillbridge.models.Trainer;
import com.college.skillbridge.models.TrainerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainerFeedbackRepository extends JpaRepository<TrainerFeedback, UUID> {
    List<TrainerFeedback> findByStudent(Student student);
    List<TrainerFeedback> findByTrainer(Trainer trainer);
    List<TrainerFeedback> findByBatch(Batch batch);
    List<TrainerFeedback> findByStudentAndBatch(Student student, Batch batch);
    
    @Query("SELECT AVG(tf.rating) FROM TrainerFeedback tf WHERE tf.student.id = :studentId")
    Double getAverageStudentRating(UUID studentId);
    
    @Query("SELECT AVG(tf.rating) FROM TrainerFeedback tf WHERE tf.batch.id = :batchId")
    Double getAverageBatchRating(UUID batchId);
}