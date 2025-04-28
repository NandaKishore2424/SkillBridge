package com.college.skillbridge.dto;

import com.college.skillbridge.enums.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressUpdateDto {
    private UUID studentId;
    private UUID batchId;
    private UUID topicId;
    private ProgressStatus status;
    private String feedback;
}