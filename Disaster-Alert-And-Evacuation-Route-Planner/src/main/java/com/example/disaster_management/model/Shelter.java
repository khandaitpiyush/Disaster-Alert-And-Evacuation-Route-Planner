package com.example.disaster_management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Shelter {


    // Getters and setters below (or use Lombok if allowed)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double latitude;
    private double longitude;
    private int capacity;
    private int occupancy;

    public Shelter() {
    }

    public Shelter(String name, double latitude, double longitude, int capacity, int occupancy) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capacity = capacity;
        this.occupancy = occupancy;
    }

    // Getters and setters...

    public boolean isFull() {
        return occupancy >= capacity;
    }

}
