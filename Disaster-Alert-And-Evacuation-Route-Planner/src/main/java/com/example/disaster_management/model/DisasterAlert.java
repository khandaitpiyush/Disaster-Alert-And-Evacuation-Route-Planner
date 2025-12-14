package com.example.disaster_management.model;

public class DisasterAlert {
    private DisasterEvent event;
    private Shelter shelter;
    private double distanceKm;

    public DisasterAlert(DisasterEvent event, Shelter shelter, double distanceKm) {
        this.event = event;
        this.shelter = shelter;
        this.distanceKm = distanceKm;
    }

    public DisasterEvent getEvent() { return event; }
    public Shelter getShelter() { return shelter; }
    public double getDistanceKm() { return distanceKm; }

    public void setEvent(DisasterEvent event) { this.event = event; }
    public void setShelter(Shelter shelter) { this.shelter = shelter; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
}
