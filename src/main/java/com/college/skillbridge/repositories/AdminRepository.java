package com.college.skillbridge.repositories;

import com.college.skillbridge.models.Admin;
import com.college.skillbridge.models.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Optional<Admin> findByEmail(String email);
    boolean existsByCollege(College college);
    boolean existsByCollege_Id(UUID collegeId);
}