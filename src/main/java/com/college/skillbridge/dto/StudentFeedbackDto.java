package com.college.skillbridge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentFeedbackDto {
    private UUID trainerId;
    private UUID batchId;
    private String comment;
    private Integer rating;
}