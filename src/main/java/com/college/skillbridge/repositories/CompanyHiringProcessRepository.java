package com.college.skillbridge.repositories;

import com.college.skillbridge.models.Company;
import com.college.skillbridge.models.CompanyHiringProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyHiringProcessRepository extends JpaRepository<CompanyHiringProcess, UUID> {
    List<CompanyHiringProcess> findByCompany(Company company);
}