package com.college.skillbridge.repositories;

import com.college.skillbridge.models.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, UUID> {
    // Custom queries can be added here
}