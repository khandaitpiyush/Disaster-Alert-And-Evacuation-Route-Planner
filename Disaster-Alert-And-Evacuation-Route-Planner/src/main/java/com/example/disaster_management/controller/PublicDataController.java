package com.example.disaster_management.controller;

import com.example.disaster_management.model.Shelter;
import com.example.disaster_management.repository.ShelterRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public") // Distinct path for public/read-only data
public class PublicDataController {

    private final ShelterRepository shelterRepository;

    public PublicDataController(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    /**
     * Handles GET requests to retrieve all Shelters for the public map display.
     * Maps to: GET /api/public/shelters
     * This is the endpoint the user-dashboard.html will call.
     */
    @GetMapping("/shelters")
    public List<Shelter> getPublicShelters() {
        // Only return necessary shelter data for map visualization
        return shelterRepository.findAll();
    }
}