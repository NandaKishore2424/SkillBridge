package com.college.skillbridge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Add this annotation to enable the builder pattern
public class BatchRecommendationDto {
    private UUID batchId;
    private String batchName;
    private String description;
    private Double totalScore;
    private List<String> matchReasons;
    private Integer skillMatchScore;
    private Integer syllabusOverlapScore;
    private Integer companyRelevanceScore;
    private Integer durationWeeks;
    private String trainerName;
    private String startDate;
}