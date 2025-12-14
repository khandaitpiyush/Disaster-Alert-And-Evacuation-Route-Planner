package com.example.disaster_management.service;

import com.example.disaster_management.model.Shelter;
import com.example.disaster_management.repository.ShelterRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final ShelterRepository shelterRepository;

    // Use constructor injection for the repository
    public DataInitializer(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    /**
     * Executes immediately after the Spring application context is fully initialized.
     * This seeds the H2 database with initial Shelter data.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadInitialShelters() {
        if (shelterRepository.count() > 0) {
            System.out.println("Shelters already exist. Skipping data initialization.");
            return;
        }

        System.out.println("--- LOADING INITIAL SHELTER DATA ---");

        // --- Mumbai Shelter Data Set ---

        // 1. Kalyan Shelter (Available, High Capacity)
        Shelter s1 = new Shelter();
        s1.setName("Kalyan Relief Center (Available)");
        s1.setLatitude(19.2366); // Approx Kalyan
        s1.setLongitude(73.1207);
        s1.setCapacity(1500);
        s1.setOccupancy(300);
        shelterRepository.save(s1);

        // 2. Mumbra Shelter (Available, Medium Capacity)
        Shelter s2 = new Shelter();
        s2.setName("Mumbra Public Safety Shelter");
        s2.setLatitude(19.1837); // Approx Mumbra
        s2.setLongitude(73.0185);
        s2.setCapacity(500);
        s2.setOccupancy(150);
        shelterRepository.save(s2);

        // 3. Bhandup Shelter (FULL - Must be ignored by allocation logic)
        Shelter s3 = new Shelter();
        s3.setName("Bhandup Community Hall (FULL)");
        s3.setLatitude(19.1491); // Approx Bhandup
        s3.setLongitude(72.9360);
        s3.setCapacity(250);
        s3.setOccupancy(250); // FULL
        shelterRepository.save(s3);

        // 4. Kurla Shelter (Available, Smaller Capacity)
        Shelter s4 = new Shelter();
        s4.setName("Kurla Municipal Shelter");
        s4.setLatitude(19.0667); // Approx Kurla
        s4.setLongitude(72.8718);
        s4.setCapacity(300);
        s4.setOccupancy(50);
        shelterRepository.save(s4);

        // 5. CST Shelter (Available, Key Location)
        Shelter s5 = new Shelter();
        s5.setName("CST Heritage Relief Point");
        s5.setLatitude(18.9401); // Approx CST Area
        s5.setLongitude(72.8354);
        s5.setCapacity(1000);
        s5.setOccupancy(100);
        shelterRepository.save(s5);

        // --- Pune Shelter Data Set ---

        // 6. Pune Shelter A (Available, Central Pune)
        Shelter s6 = new Shelter();
        s6.setName("Pune Central Command Center");
        s6.setLatitude(18.5204); // Approx Pune City
        s6.setLongitude(73.8567);
        s6.setCapacity(800);
        s6.setOccupancy(200);
        shelterRepository.save(s6);

        // 7. Pune Shelter B (Over Capacity - Also ignored by allocation logic)
        Shelter s7 = new Shelter();
        s7.setName("Hinjewadi Temporary Shelter (OVERLOADED)");
        s7.setLatitude(18.5830); // Approx Hinjewadi
        s7.setLongitude(73.7460);
        s7.setCapacity(400);
        s7.setOccupancy(500); // OVERLOADED
        shelterRepository.save(s7);

        System.out.println("Loaded " + shelterRepository.count() + " total shelters for testing.");
    }
}