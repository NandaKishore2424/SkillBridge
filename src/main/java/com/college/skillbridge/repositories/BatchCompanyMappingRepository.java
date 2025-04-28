package com.college.skillbridge.repositories;

import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.BatchCompanyMapping;
import com.college.skillbridge.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BatchCompanyMappingRepository extends JpaRepository<BatchCompanyMapping, UUID> {
    List<BatchCompanyMapping> findByBatch(Batch batch);
    List<BatchCompanyMapping> findByCompany(Company company);
}