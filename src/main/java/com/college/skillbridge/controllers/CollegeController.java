package com.college.skillbridge.controllers;

import com.college.skillbridge.repositories.CollegeRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/colleges")
public class CollegeController {

    private final CollegeRepository collegeRepository;

    public CollegeController(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    @GetMapping
    public List<CollegeSummary> listColleges() {
        return collegeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
            .stream()
            .map(college -> new CollegeSummary(
                college.getId(),
                college.getName(),
                college.getDomain(),
                college.getWebsiteUrl()))
            .collect(Collectors.toList());
    }

    public record CollegeSummary(UUID id, String name, String domain, String websiteUrl) {}
}

