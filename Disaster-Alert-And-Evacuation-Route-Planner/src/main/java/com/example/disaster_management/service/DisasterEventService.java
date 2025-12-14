package com.example.disaster_management.service;

import com.example.disaster_management.events.EventPublisher;
import com.example.disaster_management.model.DisasterAlert; // NEW: Import the DTO
import com.example.disaster_management.model.DisasterEvent;
import com.example.disaster_management.model.Shelter;
import com.example.disaster_management.repository.DisasterEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DisasterEventService {
    private final DisasterEventRepository repo;
    private final EventPublisher publisher;
    private final GeoService geoService;
    private final ShelterService shelterService;

    // NOTE: Mock User Data for 5km check.
    private static class UserLocation {
        private final Long id;
        private final String name;
        private final double latitude;
        private final double longitude;

        public UserLocation(Long id, String name, double latitude, double longitude) {
            this.id = id;
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double latitude() { return latitude; }
        public double longitude() { return longitude; }
        public String name() { return name; }
        public Long id() { return id; }
    }

    private final List<UserLocation> mockActiveUsers = List.of(
            new UserLocation(101L, "User A (Near Pune)", 18.5204, 73.8567),
            new UserLocation(102L, "User B (Near Delhi)", 28.6139, 77.2090),
            new UserLocation(103L, "User C (Far Away)", 30.0, 80.0)
    );

    // Constructor Injection
    public DisasterEventService(
            DisasterEventRepository repo,
            EventPublisher publisher,
            GeoService geoService,
            ShelterService shelterService
    ) {
        this.repo = repo;
        this.publisher = publisher;
        this.geoService = geoService;
        this.shelterService = shelterService;
    }

    public List<DisasterEvent> findAll() { return repo.findAll(); }

    public DisasterEvent findById(Long id) { return repo.findById(id).orElse(null); }

    /**
     * Creates a new disaster event, finds the nearest shelter, links the shelter ID,
     * and publishes the enriched DisasterAlert DTO via SSE.
     */
    public DisasterEvent create(DisasterEvent e) {
        // @PrePersist on the model handles the timestamp, but this acts as a fallback
        if (e.getReportedAt() == null) e.setReportedAt(LocalDateTime.now());

        DisasterEvent saved = repo.save(e);

        double alertLat = saved.getLatitude();
        double alertLon = saved.getLongitude();

        // Shelter assignment variables
        Optional<Shelter> nearestShelter = Optional.empty();
        double distanceKm = -1;

        // 1. Find the nearest available shelter
        nearestShelter = shelterService.findNearestAvailableShelter(alertLat, alertLon);

        if (nearestShelter.isPresent()) {
            // Calculate distance
            distanceKm = geoService.calculateDistanceKm(
                    alertLat, alertLon,
                    nearestShelter.get().getLatitude(), nearestShelter.get().getLongitude()
            );

            // 2. Update the persistent event with the assigned shelter's ID (CRITICAL FIX)
            saved.setAssignedShelterId(nearestShelter.get().getId());
            repo.save(saved);
        }

        // 3. User alert logic (Mock console logging)
        mockActiveUsers.stream()
                .filter(user -> geoService.isWithinAlertRadius(
                        alertLat, alertLon,
                        user.latitude(), user.longitude()
                ))
                .forEach(user -> {
                    System.out.printf(
                            "\n*** ALERT! *** Sending localized warning to %s near (Lat: %.2f, Lon: %.2f) for %s.\n",
                            user.name(), user.latitude(), user.longitude(), saved.getType() // FIX: Removed saved.getLocation()
                    );
                });

        // 4. Create and Broadcast the enriched DTO (CRITICAL FIX)
        DisasterAlert alertDTO = new DisasterAlert(saved, nearestShelter.orElse(null), distanceKm);
        publisher.publish(alertDTO); // Publishes the DTO for SSE

        System.out.println("\n[SERVICE CORE] Alert published. Nearest available shelter: " +
                (nearestShelter.isPresent() ? nearestShelter.get().getName() : "NONE"));

        return saved;
    }

    public DisasterEvent update(Long id, DisasterEvent in) {
        DisasterEvent ex = repo.findById(id).orElseThrow();
        ex.setType(in.getType());
        // FIX: Removed ex.setLocation(in.getLocation());
        ex.setSeverity(in.getSeverity());
        ex.setLatitude(in.getLatitude());
        ex.setLongitude(in.getLongitude());
        ex.setReportedAt(in.getReportedAt());

        DisasterEvent updated = repo.save(ex);
        // NOTE: If updating, you should also republish a new DisasterAlert DTO
        return updated;
    }

    public void delete(Long id) { repo.deleteById(id); }
}