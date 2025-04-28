package com.college.skillbridge.repositories;

import com.college.skillbridge.models.Project;
import com.college.skillbridge.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByStudent(Student student);
}