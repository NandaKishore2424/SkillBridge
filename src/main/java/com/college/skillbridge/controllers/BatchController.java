package com.college.skillbridge.controllers;

import com.college.skillbridge.enums.BatchStatus;
import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.Company;
import com.college.skillbridge.models.Trainer;
import com.college.skillbridge.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/batches")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @GetMapping
    public ResponseEntity<List<Batch>> getAllBatches() {
        return ResponseEntity.ok(batchService.getAllBatches());
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<Batch>> getBatchesByStatus(@RequestParam BatchStatus status) {
        return ResponseEntity.ok(batchService.getBatchesByStatus(status));
    }

    @PostMapping("/assign-trainer")
    public ResponseEntity<Void> assignTrainerToBatch(
            @RequestParam UUID trainerId, 
            @RequestParam UUID batchId) {
        batchService.assignTrainerToBatch(trainerId, batchId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/trainers")
    public ResponseEntity<List<Trainer>> getTrainersForBatch(@PathVariable UUID id) {
        return ResponseEntity.ok(batchService.getTrainersForBatch(id));
    }

    @GetMapping("/{id}/companies")
    public ResponseEntity<List<Company>> getCompaniesMappedToBatch(@PathVariable UUID id) {
        return ResponseEntity.ok(batchService.getCompaniesMappedToBatch(id));
    }
}