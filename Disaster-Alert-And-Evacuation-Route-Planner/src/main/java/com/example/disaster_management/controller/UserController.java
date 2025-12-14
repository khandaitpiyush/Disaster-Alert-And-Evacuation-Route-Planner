package com.example.disaster_management.controller;

import com.example.disaster_management.model.DisasterEvent;
import com.example.disaster_management.model.Shelter;
import com.example.disaster_management.model.UserRouteResponse;
import com.example.disaster_management.repository.ShelterRepository;
import com.example.disaster_management.service.DisasterEventService;
import com.example.disaster_management.service.GeoService;
import com.example.disaster_management.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private DisasterEventService disasterEventService;
    @Autowired private GeoService geoService;
    @Autowired private ShelterRepository shelterRepository;

    @Autowired private RouteService routeService;   // <-- NEW

    // -------------------------------
    // 1. SAFETY CHECK (Original)
    // -------------------------------
    @GetMapping("/status")
    public ResponseEntity<List<DisasterEvent>> checkAlertStatus(
            @RequestParam double lat,
            @RequestParam double lon) {

        List<DisasterEvent> activeAlerts = disasterEventService.findAll();

        List<DisasterEvent> nearbyAlerts = activeAlerts.stream()
                .filter(event -> geoService.isWithinAlertRadius(
                        lat, lon,
                        event.getLatitude(), event.getLongitude()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(nearbyAlerts);
    }

    // -------------------------------
    // 2. PUBLIC ALERTS (Original)
    // -------------------------------
    @GetMapping("/alerts")
    public ResponseEntity<List<DisasterEvent>> getAllPublicAlerts() {
        return ResponseEntity.ok(disasterEventService.findAll());
    }

    // -------------------------------
    // 3. PUBLIC SHELTERS (Original)
    // -------------------------------
    @GetMapping("/shelters")
    public ResponseEntity<List<Shelter>> getPublicShelters() {
        List<Shelter> availableShelters = shelterRepository.findAll()
                .stream()
                .filter(s -> s.getOccupancy() < s.getCapacity())
                .collect(Collectors.toList());

        return ResponseEntity.ok(availableShelters);
    }

    // --------------------------------------------------
    // 4. NEW: SHORTEST ROUTE TO NEAREST SHELTER
    // --------------------------------------------------
    @GetMapping("/route")
    public ResponseEntity<UserRouteResponse> getRouteToNearestShelter(
            @RequestParam double lat,
            @RequestParam double lon) {

        // Step 1: Get all shelters
        List<Shelter> shelters = shelterRepository.findAll()
                .stream()
                .filter(s -> s.getOccupancy() < s.getCapacity())
                .collect(Collectors.toList());

        // Step 2: Find nearest shelter
        Shelter nearest = shelters.stream()
                .min((s1, s2) -> Double.compare(
                        geoService.calculateDistance(lat, lon, s1.getLatitude(), s1.getLongitude()),
                        geoService.calculateDistance(lat, lon, s2.getLatitude(), s2.getLongitude())
                ))
                .orElse(null);

        if (nearest == null) {
            return ResponseEntity.noContent().build();
        }

        double distance = geoService.calculateDistance(
                lat, lon,
                nearest.getLatitude(),
                nearest.getLongitude()
        );

        // Step 3: Get route from OSRM through RouteService
        String routeJson = routeService.getRoute(
                lat, lon,
                nearest.getLatitude(),
                nearest.getLongitude()
        );

        // Step 4: Bundle in DTO
        UserRouteResponse response = new UserRouteResponse();
        response.setNearestShelter(nearest.getName());
        response.setDistance(distance);
        response.setLatitude(nearest.getLatitude());
        response.setLongitude(nearest.getLongitude());
        response.setRouteGeoJson(routeJson);

        return ResponseEntity.ok(response);
    }
}
