package com.college.skillbridge.service;

import com.college.skillbridge.models.Company;
import com.college.skillbridge.models.CompanyHiringProcess;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
    List<Company> getAllCompanies();
    List<Company> getCompaniesByDomain(String domain);
    List<CompanyHiringProcess> getHiringProcess(UUID companyId);
}