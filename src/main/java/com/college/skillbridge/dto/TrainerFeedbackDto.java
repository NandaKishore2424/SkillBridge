package com.college.skillbridge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerFeedbackDto {
    private UUID studentId;
    private UUID batchId;
    private String content;
    private Integer rating;
}