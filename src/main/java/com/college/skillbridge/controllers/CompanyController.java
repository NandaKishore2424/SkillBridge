package com.college.skillbridge.controllers;

import com.college.skillbridge.models.Company;
import com.college.skillbridge.models.CompanyHiringProcess;
import com.college.skillbridge.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/by-domain")
    public ResponseEntity<List<Company>> getCompaniesByDomain(@RequestParam String domain) {
        return ResponseEntity.ok(companyService.getCompaniesByDomain(domain));
    }

    @GetMapping("/{id}/hiring-process")
    public ResponseEntity<List<CompanyHiringProcess>> getHiringProcess(@PathVariable UUID id) {
        return ResponseEntity.ok(companyService.getHiringProcess(id));
    }
}