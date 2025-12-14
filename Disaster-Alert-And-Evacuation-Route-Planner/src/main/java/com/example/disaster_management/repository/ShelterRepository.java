package com.example.disaster_management.repository;

import com.example.disaster_management.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    // No extra methods needed for now
}
