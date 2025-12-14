package com.example.disaster_management.service;

import com.example.disaster_management.model.Shelter;
import com.example.disaster_management.repository.ShelterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final GeoService geoService;

    public ShelterService(ShelterRepository shelterRepository, GeoService geoService) {
        this.shelterRepository = shelterRepository;
        this.geoService = geoService;
    }

    /**
     * Finds the nearest available (not full) shelter to the given geographical coordinates.
     * @param lat User or disaster latitude
     * @param lon User or disaster longitude
     * @return Optional<Shelter> nearest available shelter
     */
    public Optional<Shelter> findNearestAvailableShelter(double lat, double lon) {

        List<Shelter> shelters = shelterRepository.findAll();

        Shelter nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Shelter shelter : shelters) {

            // Skip full shelters
            if (shelter.isFull()) continue;

            double distance = geoService.calculateDistance(
                    lat, lon,
                    shelter.getLatitude(), shelter.getLongitude()
            );

            if (distance < minDistance) {
                minDistance = distance;
                nearest = shelter;
            }
        }

        return Optional.ofNullable(nearest);
    }
}
