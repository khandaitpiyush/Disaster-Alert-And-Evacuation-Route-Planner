package com.example.disaster_management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class RouteService {

    private static final Logger logger = LoggerFactory.getLogger(RouteService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    public String getRoute(double userLat, double userLon,
                           double shelterLat, double shelterLon) {

        // Build OSRM Request URL
        String url = String.format(
                "https://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=full&geometries=geojson",
                userLon, userLat, shelterLon, shelterLat
        );

        logger.info("Requesting OSRM Route: {}", url);

        try {
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            logger.error("OSRM API error: {}", e.getMessage());
            return "{}";   // Safe fallback
        }
    }
}
