package com.college.skillbridge.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class BatchResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String type; // DOCUMENT, VIDEO, CODE_SAMPLE, etc.

    @Column(nullable = false)
    private String url; // Could be a file path or external URL

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    private boolean isRequired = true;

    @Column(columnDefinition = "TEXT")
    private String notes;
}