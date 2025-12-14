package com.example.disaster_management.model;

public class UserRouteResponse {

    private String nearestShelter;
    private double distance;

    private double latitude;     // shelter latitude
    private double longitude;    // shelter longitude

    private String routeGeoJson; // full OSRM route (GeoJSON string)

    // Getters & Setters
    public String getNearestShelter() {
        return nearestShelter;
    }

    public void setNearestShelter(String nearestShelter) {
        this.nearestShelter = nearestShelter;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRouteGeoJson() {
        return routeGeoJson;
    }

    public void setRouteGeoJson(String routeGeoJson) {
        this.routeGeoJson = routeGeoJson;
    }
}
