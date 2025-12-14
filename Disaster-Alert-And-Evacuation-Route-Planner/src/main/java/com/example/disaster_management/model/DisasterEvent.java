package com.example.disaster_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a disaster event reported in the system.
 */
@Setter
@Getter
@Entity
public class DisasterEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Type cannot be empty")
    private String type;

    @NotBlank(message = "Severity cannot be empty")
    private String severity;

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private double longitude;

    private LocalDateTime reportedAt;

    private Long assignedShelterId;

    // --- NEW FIELD ---
    // This field is used by the EventPublisher but is NOT saved to the database.
    @Transient
    private String nearestShelterInfo;


    // --- Constructors ---

    public DisasterEvent() {
        // Default constructor required by JPA
    }

    // --- Lifecycle Method ---
    @PrePersist
    protected void onCreate() {
        reportedAt = LocalDateTime.now();
    }
}