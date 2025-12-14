package com.example.disaster_management.service;

import org.springframework.stereotype.Service;

/**
 * Service to handle geographical calculations, such as distance between two points
 * using the Haversine formula.
 */
@Service
public class GeoService {

    // Earth's radius in kilometers (standard for Haversine)
    private static final double EARTH_RADIUS_KM = 6371;

    // Alert radius for user safety status (5 km)
    private static final double ALERT_RADIUS_KM = 5.0;

    /**
     * Calculates the distance between two points on the Earth's surface using the Haversine formula.
     * @return The distance in kilometers
     */
    public double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Shorthand method for ease of use with controller code.
     * Internally calls calculateDistanceKm().
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return calculateDistanceKm(lat1, lon1, lat2, lon2);
    }

    /**
     * Checks if two coordinates are within the alert radius (5 km).
     */
    public boolean isWithinAlertRadius(double lat1, double lon1, double lat2, double lon2) {
        double distance = calculateDistanceKm(lat1, lon1, lat2, lon2);
        return distance <= ALERT_RADIUS_KM;
    }
}
