package com.college.skillbridge.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("SkillBridge backend is working! Database connection successful.");
    }
}