package com.example.disaster_management.controller;

import com.example.disaster_management.events.EventPublisher;
import com.example.disaster_management.model.DisasterEvent;
import com.example.disaster_management.model.Shelter;
import com.example.disaster_management.repository.DisasterEventRepository;
import com.example.disaster_management.repository.ShelterRepository;
import com.example.disaster_management.service.DisasterEventService;
import com.example.disaster_management.service.GeoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AlertController {

    @Autowired
    private DisasterEventService disasterEventService;

    @Autowired
    private DisasterEventRepository disasterEventRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private GeoService geoService;

    @Autowired
    private EventPublisher eventPublisher;

    // ------------------------------------------------------------
    // 1. CREATE ALERT (POST) — Includes SSE Broadcast
    // ------------------------------------------------------------
    @PostMapping("/alerts")
    public ResponseEntity<DisasterEvent> createAlert(@Valid @RequestBody DisasterEvent event) {

        // 1️⃣ Save alert
        DisasterEvent newEvent = disasterEventService.create(event);

        // 2️⃣ Broadcast it to all SSE subscribers (USER DASHBOARD)
        eventPublisher.publishAlert(newEvent);

        // 3️⃣ Return response
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }

    // ------------------------------------------------------------
    // 2. GET ALL ACTIVE ALERTS
    // ------------------------------------------------------------
    @GetMapping("/alerts/active")
    public ResponseEntity<List<DisasterEvent>> getAllActiveAlerts() {
        return ResponseEntity.ok(disasterEventRepository.findAll());
    }

    // ------------------------------------------------------------
    // 3. SEARCH SHELTERS WITHIN RADIUS
    // ------------------------------------------------------------
    @GetMapping("/shelters/search")
    public ResponseEntity<List<Shelter>> getSheltersWithinRadius(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        List<Shelter> allShelters = shelterRepository.findAll();

        List<Shelter> sheltersInRadius = allShelters.stream()
                .filter(shelter ->
                        geoService.isWithinAlertRadius(
                                lat,
                                lon,
                                shelter.getLatitude(),
                                shelter.getLongitude()
                        )
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(sheltersInRadius);
    }

    // ------------------------------------------------------------
    // 4. REAL-TIME STREAM (SSE)
    // ------------------------------------------------------------
    @GetMapping("/stream/events")
    public SseEmitter streamEvents() {
        return eventPublisher.addEmitter();
    }
}
