package com.college.skillbridge.repositories;

import com.college.skillbridge.models.Student;
import com.college.skillbridge.models.StudentBatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentBatchHistoryRepository extends JpaRepository<StudentBatchHistory, UUID> {
    List<StudentBatchHistory> findByStudent(Student student);
}