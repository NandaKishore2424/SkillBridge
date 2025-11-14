package com.college.skillbridge.repositories;

import com.college.skillbridge.models.College;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, UUID> {
    Optional<College> findByDomain(String domain);
    boolean existsByNameIgnoreCase(String name);
    Optional<College> findByNameIgnoreCase(String name);
}

