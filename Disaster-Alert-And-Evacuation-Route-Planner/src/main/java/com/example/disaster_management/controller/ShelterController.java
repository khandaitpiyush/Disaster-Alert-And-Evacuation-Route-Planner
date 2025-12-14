package com.example.disaster_management.controller;

import com.example.disaster_management.model.Shelter;
import com.example.disaster_management.repository.ShelterRepository;
import com.example.disaster_management.service.ShelterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shelters")
public class ShelterController {

    private final ShelterRepository shelterRepository;
    private final ShelterService shelterService;

    // Constructor Injection
    public ShelterController(ShelterRepository shelterRepository, ShelterService shelterService) {
        this.shelterRepository = shelterRepository;
        this.shelterService = shelterService;
    }

    // ----------------------------------------------------------------------
    // C - CREATE (POST /api/shelters)
    // ----------------------------------------------------------------------

    @PostMapping
    public ResponseEntity<Object> createShelter(@RequestBody Shelter shelter) {
        if (shelter.getOccupancy() > shelter.getCapacity()) {
            return new ResponseEntity<>("Error: Occupancy cannot exceed capacity.", HttpStatus.BAD_REQUEST);
        }
        Shelter newShelter = shelterRepository.save(shelter);
        return new ResponseEntity<>(newShelter, HttpStatus.CREATED);
    }

    // ----------------------------------------------------------------------
    // R - READ ALL (GET /api/shelters)
    // ----------------------------------------------------------------------

    @GetMapping
    public List<Shelter> getAllShelters() {
        return shelterRepository.findAll();
    }

    // ----------------------------------------------------------------------
    // R - READ NEAREST (GET /api/shelters/nearest?lat=X&lon=Y)
    // ----------------------------------------------------------------------

    /**
     * Finds the nearest available shelter based on provided coordinates.
     * This method resolves the client-side 405 error and the type compilation error.
     */
    @GetMapping("/nearest")
    public ResponseEntity<Shelter> getNearestShelter(
            @RequestParam double lat,
            @RequestParam double lon) {

        // The service method correctly returns Optional<Shelter>
        Optional<Shelter> nearestShelter = shelterService.findNearestAvailableShelter(lat, lon);

        // 🚨 FIX: Safely extract the Shelter object if present, otherwise return 404.
        if (nearestShelter.isPresent()) {
            // Returns 200 OK with the Shelter object in the body
            return new ResponseEntity<>(nearestShelter.get(), HttpStatus.OK);
        } else {
            // Returns 404 NOT FOUND if no available shelter is found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ----------------------------------------------------------------------
    // U - UPDATE (PUT /api/shelters/{id})
    // ----------------------------------------------------------------------

    /**
     * Handles PUT requests to update an existing Shelter's data (used primarily for occupancy).
     * Assumes Shelter model uses Integer/Double wrapper types for safe null checks.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateShelter(@PathVariable Long id, @RequestBody Shelter updatedShelter) {
        Optional<Shelter> shelterOptional = shelterRepository.findById(id);

        if (!shelterOptional.isPresent()) {
            return new ResponseEntity<>("Shelter not found with ID: " + id, HttpStatus.NOT_FOUND);
        }

        Shelter shelter = shelterOptional.get();

        Integer newCapacity = updatedShelter.getCapacity();
        Integer newOccupancy = updatedShelter.getOccupancy();

        // Determine the final capacity for validation (use existing if new is null)
        Integer finalCapacity = (newCapacity != null) ? newCapacity : shelter.getCapacity();

        // 1. Validation: Check if the new occupancy exceeds the final capacity
        if (newOccupancy != null && newOccupancy > finalCapacity) {
            return new ResponseEntity<>("Error: Occupancy (" + newOccupancy + ") cannot exceed capacity (" + finalCapacity + ").", HttpStatus.BAD_REQUEST);
        }

        // 2. Apply non-null updates
        if (updatedShelter.getName() != null) shelter.setName(updatedShelter.getName());
        if (newCapacity != null) shelter.setCapacity(newCapacity);
        if (newOccupancy != null) shelter.setOccupancy(newOccupancy);

        // 3. Save the updated entity
        Shelter savedShelter = shelterRepository.save(shelter);

        return new ResponseEntity<>(savedShelter, HttpStatus.OK);
    }

    // ----------------------------------------------------------------------
    // D - DELETE (DELETE /api/shelters/{id})
    // ----------------------------------------------------------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelter(@PathVariable Long id) {
        if (!shelterRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        shelterRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }
}