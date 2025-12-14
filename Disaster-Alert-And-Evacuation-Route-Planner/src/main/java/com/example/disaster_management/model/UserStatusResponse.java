package com.example.disaster_management.model;

public class UserStatusResponse {
    private boolean inDanger;
    private String nearestShelter;
    private double distanceToShelter;
    private String routeGeoJson; // NEW

    // getters and setters
    public boolean isInDanger() { return inDanger; }
    public void setInDanger(boolean inDanger) { this.inDanger = inDanger; }

    public String getNearestShelter() { return nearestShelter; }
    public void setNearestShelter(String nearestShelter) { this.nearestShelter = nearestShelter; }

    public double getDistanceToShelter() { return distanceToShelter; }
    public void setDistanceToShelter(double distanceToShelter) { this.distanceToShelter = distanceToShelter; }

    public String getRouteGeoJson() { return routeGeoJson; }
    public void setRouteGeoJson(String routeGeoJson) { this.routeGeoJson = routeGeoJson; }
}
