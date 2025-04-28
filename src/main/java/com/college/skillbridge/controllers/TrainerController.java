package com.college.skillbridge.controllers;

import com.college.skillbridge.models.Batch;
import com.college.skillbridge.models.Trainer;
import com.college.skillbridge.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @GetMapping
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        return ResponseEntity.ok(trainerService.getAllTrainers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trainer> getTrainerById(@PathVariable UUID id) {
        return ResponseEntity.ok(trainerService.getTrainerById(id));
    }

    @GetMapping("/{id}/batches")
    public ResponseEntity<List<Batch>> getBatchesAssigned(@PathVariable UUID id) {
        return ResponseEntity.ok(trainerService.getBatchesAssigned(id));
    }
}