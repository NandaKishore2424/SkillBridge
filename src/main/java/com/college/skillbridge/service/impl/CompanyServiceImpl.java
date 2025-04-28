package com.college.skillbridge.service.impl;

import com.college.skillbridge.models.Company;
import com.college.skillbridge.models.CompanyHiringProcess;
import com.college.skillbridge.repositories.CompanyHiringProcessRepository;
import com.college.skillbridge.repositories.CompanyRepository;
import com.college.skillbridge.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private CompanyHiringProcessRepository companyHiringProcessRepository;

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> getCompaniesByDomain(String domain) {
        return companyRepository.findAll().stream()
                .filter(company -> domain.equals(company.getDomain()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyHiringProcess> getHiringProcess(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
        
        return companyHiringProcessRepository.findByCompany(company);
    }
}